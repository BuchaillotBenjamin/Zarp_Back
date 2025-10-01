package org.example.zarp_back.service;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.CredencialesMP;
import org.example.zarp_back.model.entity.CredencialesPP;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.EstadoPayout;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.example.zarp_back.service.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class PaypalService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CryptoUtils cryptoUtils;

    @Autowired
    private ReservaRepository reservaRepository;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.partner-attribution-id}")
    private String partnerAttributionId;

    @Value("${paypal.mode}")
    private String environment;

    @Value("${api.url}")
    private String apiUrl;

    private PayPalHttpClient payPalHttpClient;

    private String accessToken;
    private Instant tokenExpiry;

    private RestTemplate restTemplate = new RestTemplate();


    private final Map <String, Cliente>clientesTemporales= new HashMap<>();
    private final Map <String, ReservaDTO> reservasTemporales = new HashMap();




    public String createPayout(ReservaDTO reserva) {
        if (!reservaService.esReservaValida(reserva)) {
            log.error("Reserva inválida: {}", reserva);
            throw new IllegalArgumentException("Reserva inválida");
        }

        // Obtener datos del destinatario
        Propiedad propiedad = propiedadRepository.findById(reserva.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada"));

        Cliente anfitrion = propiedad.getPropietario();

        if (anfitrion.getCredencialesPP() == null) {
            log.error("El anfitrión no tiene credenciales de PayPal configuradas");
            throw new RuntimeException("El anfitrión no tiene credenciales de PayPal configuradas");
        }

        String emailDestino = anfitrion.getCredencialesPP().getMailPaypal();

        if (emailDestino == null || !emailDestino.contains("@")) {
            log.error("Email de destinatario inválido: {}", emailDestino);
            throw new IllegalArgumentException("El email del destinatario no es válido");
        }

        // Cálculo de comisión
        double montoTotal = reserva.getPrecioTotal();
        double comision = montoTotal * 0.10;
        double montoAnfitrion = montoTotal - comision;

        if (montoAnfitrion <= 0) {
            log.error("Monto inválido para payout: {}", montoAnfitrion);
            throw new IllegalArgumentException("El monto del payout debe ser mayor a cero");
        }

        String montoFormateado = String.format(Locale.US, "%.2f", montoAnfitrion);
        String tempId = UUID.randomUUID().toString();

        // Construir cuerpo del payout
        Map<String, Object> payoutItem = new HashMap<>();
        payoutItem.put("recipient_type", "EMAIL");
        payoutItem.put("amount", Map.of("value", montoFormateado, "currency", "USD")); // ← moneda corregida
        payoutItem.put("receiver", emailDestino);
        payoutItem.put("note", "Pago por reserva #" + propiedad.getNombre() + ". Comisión retenida: $" + String.format("%.2f", comision));
        payoutItem.put("sender_item_id", tempId);

        Map<String, Object> payoutBody = new HashMap<>();
        payoutBody.put("sender_batch_header", Map.of(
                "sender_batch_id", tempId,
                "email_subject", "Has recibido un pago de Zarp"
        ));
        payoutBody.put("items", List.of(payoutItem));

        // Autenticación
        String accessToken = getOrRefreshAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payoutBody, headers);

        String url = "https://api-m." + (environment.equals("sandbox") ? "sandbox." : "") + "paypal.com/v1/payments/payouts";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> body = response.getBody();

            reservasTemporales.put(tempId, reserva);

            log.info("Payout creado: monto anfitrión={}, comisión={}, itemId={}", montoFormateado, comision, tempId);

            return body != null ? body.get("batch_header").toString() : "Sin respuesta";
        } catch (Exception e) {
            log.error("Error al crear payout: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear el payout");
        }
    }


    public boolean procesarWebhookPayout(Map<String, Object> payload) {
        String eventType = (String) payload.get("event_type");
        Map<String, Object> resource = (Map<String, Object>) payload.get("resource");
        Map<String, Object> payoutItem = (Map<String, Object>) resource.get("payout_item");
        String senderItemId = (String) payoutItem.get("sender_item_id");

        log.info("Procesando webhook de payout: eventType={}, senderItemId={}", eventType, senderItemId);

        EstadoPayout estadoInterno;

        switch (eventType) {
            case "PAYMENT.PAYOUTS-ITEM.SUCCEEDED":
                estadoInterno = EstadoPayout.PAGADO;
                break;
            case "PAYMENT.PAYOUTS-ITEM.FAILED":
                estadoInterno =EstadoPayout.FALLIDO;
                break;
            case "PAYMENT.PAYOUTS-ITEM.UNCLAIMED":
                estadoInterno = EstadoPayout.NO_RECLAMADO;
                break;
            default:
                log.warn("Evento no esperado: {}. Marcando como CANCELADO", eventType);
                estadoInterno = EstadoPayout.CANCELADO;
        }

        ReservaDTO reserva = reservasTemporales.get(senderItemId);

        if (reserva != null) {
            boolean exito = procesarReserva(reserva, estadoInterno);
            if (exito) {
                reservasTemporales.remove(senderItemId);
            }
            return exito;
        } else {
            log.warn("No se encontró reserva para itemId {}", senderItemId);
            return false;
        }
    }

    @Transactional
    public boolean procesarReserva(ReservaDTO reserva, EstadoPayout estado) {
        ReservaResponseDTO reservaGuardada;

        switch (estado) {
            case PAGADO:
                reservaGuardada = reservaService.save(reserva);
                reservaService.cambiarEstado(reservaGuardada.getId(), Estado.RESERVADA);
                log.info("Reserva creada con ID: {}", reservaGuardada.getId());
                return true;

            case NO_RECLAMADO:
                log.info("Reserva no reclamada");
                return true;

            case CANCELADO:
            case FALLIDO:
            default:
                reservaGuardada = reservaService.save(reserva);
                reservaService.cambiarEstado(reservaGuardada.getId(), Estado.CANCELADA);
                log.info("Reserva cancelada con ID: {}", reservaGuardada.getId());
                return true;
        }
    }

    @Transactional
    public Boolean guardarDireccionPaypal(Long clienteId, String direccionPaypal) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (cliente.getRol() != Rol.PROPIETARIO) {
            throw new IllegalArgumentException("Solo los propietarios pueden guardar una dirección de PayPal");
        }
        if (cliente.getCredencialesPP()!=null){
            throw new IllegalArgumentException("El cliente ya tiene una dirección de PayPal guardada");
        }
        CredencialesPP credencialesPP = new CredencialesPP();
        credencialesPP.setMailPaypal(direccionPaypal);
        cliente.setCredencialesPP(credencialesPP);
        clienteRepository.save(cliente);

        return true;
    }

    private String obtenerAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String url = "https://api-m." + (environment.equals("sandbox") ? "sandbox." : "") + "paypal.com/v1/oauth2/token";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        Integer expiresIn = (Integer) responseBody.get("expires_in");
        tokenExpiry = Instant.now().plusSeconds(expiresIn);
        return responseBody != null ? responseBody.get("access_token").toString() : null;
    }

    private String getOrRefreshAccessToken() {
        if (accessToken == null || Instant.now().isAfter(tokenExpiry)) {
            accessToken = obtenerAccessToken(); // tu método actual
        }
        return accessToken;
    }


}