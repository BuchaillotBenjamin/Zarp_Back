package org.example.zarp_back.service;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.*;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.EstadoPayout;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaPayoutPendienteRepository;
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

    @Autowired
    private ReservaPayoutPendienteRepository reservaPayoutPendienteRepository;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String environment;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${mercadopago.back_url.success}")
    private String backUrlSuccess;

    @Value("${mercadopago.back_url.failure}")
    private String backUrlFailure;

    private PayPalHttpClient payPalHttpClient;

    private String accessToken;
    private Instant tokenExpiry;

    private RestTemplate restTemplate = new RestTemplate();


    private final Map <String, ReservaDTO> reservasTemporales = new HashMap();
    @Autowired
    private ClienteService clienteService;


    //payout a propietario
    public String createPayout(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));


        Cliente anfitrion = reserva.getPropiedad().getPropietario();


        String emailDestino = anfitrion.getCredencialesPP().getMailPaypal();

        if (emailDestino == null || !emailDestino.contains("@")) {
            log.error("Email de destinatario inválido: {}", emailDestino);
            throw new IllegalArgumentException("El email del destinatario no es válido");
        }

        // Cálculo de comisión y conversión
        double precioTotal = reserva.getPrecioTotal(); // Ej: $87.000
        double comision = precioTotal * 0.10;
        double montoAnfitrion = precioTotal - comision;

        if (montoAnfitrion <= 0) {
            log.error("Monto inválido para payout: {}", montoAnfitrion);
            throw new IllegalArgumentException("El monto del payout debe ser mayor a cero");
        }

        BigDecimal cotizacionVenta = obtenerCotizacionDolar(); // Ej: $870
        BigDecimal montoEnPesos = BigDecimal.valueOf(montoAnfitrion);
        BigDecimal montoUSD = montoEnPesos.divide(cotizacionVenta, 2, RoundingMode.HALF_UP);
        String montoFormateado = String.format(Locale.US, "%.2f", montoUSD);


        log.info("Monto convertido a USD: {} (ARS={} / cotización={})", montoFormateado, montoAnfitrion, cotizacionVenta);
        String tempId = UUID.randomUUID().toString();

        // Construir cuerpo del payout
        Map<String, Object> payoutItem = new HashMap<>();
        payoutItem.put("recipient_type", "EMAIL");
        payoutItem.put("amount", Map.of("value", montoFormateado, "currency", "USD")); // ← moneda corregida
        payoutItem.put("receiver", emailDestino);
        payoutItem.put("note", "Pago por reserva #" + reserva.getPropiedad().getNombre() + ". Comisión retenida: $" + String.format("%.2f", comision));
        payoutItem.put("sender_item_id",reservaId.toString());

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

        switch (eventType) {
            case "PAYMENT.PAYOUTS-ITEM.SUCCEEDED":
                log.info("Payout exitoso para itemId={}", senderItemId);
                ReservaPayoutPendiente reservaPayoutPendiente = reservaPayoutPendienteRepository.findByReservaId(Long.parseLong(senderItemId));
                reservaPayoutPendiente.setActivo(false);
                reservaPayoutPendienteRepository.save(reservaPayoutPendiente);
                break;
            case "PAYMENT.PAYOUTS-ITEM.FAILED":
                log.info("Payout fallido para itemId={}", senderItemId);
                break;
            case "PAYMENT.PAYOUTS-ITEM.UNCLAIMED":
                log.info("Payout no reclamado para itemId={}", senderItemId);
                break;
            default:
                log.warn("Evento no esperado: {}", eventType);
        }

        return true;

    }

    //orden de pago a cliente
    public String createPayPalOrder(ReservaDTO reserva) {
        if (!reservaService.esReservaValida(reserva)) {
            log.error("Reserva inválida: {}", reserva);
            throw new IllegalArgumentException("Reserva inválida");
        }
        Propiedad propiedad = propiedadRepository.findById(reserva.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada"));
        Cliente anfitrion = propiedad.getPropietario();

        if (anfitrion.getCredencialesPP() == null) {
            log.error("El anfitrión no tiene credenciales de PayPal configuradas");
            throw new RuntimeException("El anfitrión no tiene credenciales de PayPal configuradas");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getOrRefreshAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        BigDecimal precioEnPesos = new BigDecimal(reserva.getPrecioTotal()); // Ej: $87.000
        BigDecimal cotizacionVenta = obtenerCotizacionDolar(); // Ej: $870

        BigDecimal precioUSD = precioEnPesos.divide(cotizacionVenta, 2, RoundingMode.HALF_UP);
        String valorConvertido = precioUSD.toPlainString();

        String tempId = UUID.randomUUID().toString(); // o cualquier lógica tuya

        reservasTemporales.put(tempId, reserva);

        Map<String, Object> order = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "amount", Map.of(
                                "currency_code", "USD",
                                "value", valorConvertido
                        ),
                        "custom_id", tempId
                )),
                "application_context", Map.of(
                        "return_url", backUrlSuccess ,
                        "cancel_url", backUrlFailure
                )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(order, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api-m.sandbox.paypal.com/v2/checkout/orders", request, Map.class);

        List<Map<String, String>> links = (List<Map<String, String>>) response.getBody().get("links");

        log.info("Orden de pago creada: tempId={}, orderId={}", tempId, response.getBody().get("id"));

        return links.stream()
                .filter(link -> "approve".equals(link.get("rel")))
                .findFirst()
                .map(link -> link.get("href"))
                .orElseThrow(() -> new RuntimeException("No se encontró el link de aprobación"));
    }

    public Boolean procesarWebhookOrdenPago(Map<String, Object> payload) {
        String eventType = (String) payload.get("event_type");
        Map<String, Object> resource = (Map<String, Object>) payload.get("resource");

        if (resource == null) {
            log.warn("Webhook sin recurso válido");
            return false;
        }

        String tempId = null;
        List<Map<String, Object>> purchaseUnits = null;

        // Manejo según el tipo de evento
        if ("CHECKOUT.ORDER.APPROVED".equals(eventType) || "CHECKOUT.ORDER.COMPLETED".equals(eventType)) {
            purchaseUnits = (List<Map<String, Object>>) resource.get("purchase_units");
            if (purchaseUnits != null && !purchaseUnits.isEmpty()) {
                tempId = (String) purchaseUnits.get(0).get("custom_id");
            }
        } else if ("PAYMENT.CAPTURE.COMPLETED".equals(eventType) ||
                "PAYMENT.CAPTURE.PENDING".equals(eventType) ||
                "PAYMENT.CAPTURE.DENIED".equals(eventType) ||
                "PAYMENT.CAPTURE.DECLINED".equals(eventType)) {
            tempId = (String) resource.get("custom_id");
        }

        String status = (String) resource.get("status");
        log.info("Procesando webhook de orden de pago: eventType={}, status={}", eventType, status);

        EstadoPayout estadoInterno;
        boolean capturado = false;

        switch (status) {
            case "COMPLETED":
                estadoInterno = EstadoPayout.PAGADO;
                break;
            case "DENIED":
                estadoInterno = EstadoPayout.FALLIDO;
                break;
            case "PENDING":
                return true; // Esperar a que se complete
            case "APPROVED":
                capturarPago((String) resource.get("id"));
                log.info("esperar otro webhook de paypal");
                return true; // Esperar a que se complete

            default:
                log.warn("Estado no esperado: {}. Marcando como CANCELADO", status);
                return false;
        }

        if (tempId == null) {
            log.warn("Webhook recibido sin custom_id");
            return false;
        }

        log.info("Webhook recibido para tempId: {}", tempId);

        ReservaDTO reserva = reservasTemporales.get(tempId);

        if (reserva == null) {
            log.error("No se encontró reserva para tempId: {}", tempId);
            return false;
        }else{
            reservasTemporales.remove(tempId);
            log.info("Reserva procesada y eliminada para tempId: {}, estado: {}", tempId, estadoInterno);
        }

        procesarReserva(reserva, estadoInterno);


        return true;
    }

    //persistencia de la reserva
    @Transactional
    public void procesarReserva(ReservaDTO reserva, EstadoPayout estado) {
        ReservaResponseDTO reservaGuardada;

        switch (estado) {
            case PAGADO:
                reservaGuardada = reservaService.save(reserva);

                ReservaPayoutPendiente reservaPayoutPendiente = new ReservaPayoutPendiente().builder()
                        .reservaId(reservaGuardada.getId())
                        .build();
                reservaPayoutPendienteRepository.save(reservaPayoutPendiente);
                createPayout(reservaGuardada.getId());
                log.info("ReservaPayoutPendiente creada para reserva ID: {}", reservaGuardada.getId());

                reservaService.cambiarEstado(reservaGuardada.getId(), Estado.RESERVADA);
                log.info("Reserva creada con ID: {}", reservaGuardada.getId());
                break;

            case NO_RECLAMADO:
                log.info("Reserva no reclamada");
                break;

            case CANCELADO:
            case FALLIDO:
            default:
                reservaGuardada = reservaService.save(reserva);
                reservaService.cambiarEstado(reservaGuardada.getId(), Estado.CANCELADA);
                log.info("Reserva cancelada con ID: {}", reservaGuardada.getId());
                break;

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
        clienteService.actualizarAutorizaciones(clienteId);

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

    private void capturarPago(String orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getOrRefreshAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture",
                    request,
                    Map.class
            );

            Map<String, Object> body = response.getBody();

            if (body == null) {
                log.warn("Respuesta vacía al capturar pago");

            }

            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) body.get("purchase_units");
            if (purchaseUnits == null || purchaseUnits.isEmpty()) {
                log.warn("No se encontraron purchase_units en la captura");
            }

            Map<String, Object> payments = (Map<String, Object>) purchaseUnits.get(0).get("payments");
            if (payments == null) {
                log.warn("No se encontraron datos de pagos en la captura");
            }

            List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
            if (captures == null || captures.isEmpty()) {
                log.warn("No se encontraron capturas en la respuesta");
            }

            String status = (String) captures.get(0).get("status");
            log.info("Estado de la captura: {}", status);


            log.info("Pago capturado exitosamente para orderId={}", orderId);

        } catch (Exception e) {
            log.error("Error al capturar el pago para orderId={}: {}", orderId, e.getMessage());
        }
    }

    private BigDecimal obtenerCotizacionDolar() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "https://dolarapi.com/v1/dolares/oficial", Map.class
        );
        Map<String, Object> body = response.getBody();
        return new BigDecimal(body.get("venta").toString());
    }


}