package org.example.zarp_back.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.CredencialesMP;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.AutorizacionesCliente;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.example.zarp_back.service.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MercadoPagoService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    PropiedadRepository propiedadRepository;
    @Autowired
    ReservaRepository ReservaRepository;
    @Value("${mercadopago.access_token}")
    private String mpAccess;
    @Value("${mercadopago.back_url.success}")
    private String mpSuccessBackUrl;
    @Value("${mercadopago.back_url.pending}")
    private String mpPendingBackUrl;
    @Value("${mercadopago.back_url.failure}")
    private String mpFailureBackUrl;
    @Value("${api.url}")
    private String publicUrl;
    @Value("${mercadopago.client_id}")
    private String mpClientId;
    @Value("${mercadopago.client_secret}")
    private String mpClientSecret;
    private final CryptoUtils cryptoUtils;
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    public MercadoPagoService(CryptoUtils cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    // Mapa temporal
    private static final Map<String, ReservaResponseDTO> reservasTemporales = new ConcurrentHashMap<>();
    private static final Map<String, Cliente> clientesTemporales = new ConcurrentHashMap<>();

    /*@PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccess);
    }*/

    public Preference createPreference(ReservaDTO reserva) throws MPException, MPApiException {

        Propiedad propiedad = propiedadRepository.findById(reserva.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada"));
        Cliente vendedor = propiedad.getPropietario();

        if (vendedor.getCredencialesMP() == null) {
            throw new RuntimeException("El vendedor no tiene credenciales de Mercado Pago");
        }

        String tokenVendedor;
        ReservaResponseDTO reservaEntidad = reservaService.save(reserva);

        try {
            tokenVendedor = cryptoUtils.decrypt(vendedor.getCredencialesMP().getAccessToken());
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar las credenciales");
        }

        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Reserva temporal Zarp")
                .quantity(1)
                .unitPrice(new BigDecimal(reserva.getPrecioTotal()))
                .build();
        items.add(itemRequest);

        Double fee = reserva.getPrecioTotal() * 0.10;
        Cliente comprador = clienteRepository.findById(reserva.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .marketplaceFee(new BigDecimal(fee))
                .externalReference(String.valueOf(reservaEntidad.getId())) // 🔑 clave para vincular el pago
                .payer(
                        PreferencePayerRequest.builder()
                                .email(comprador.getCorreoElectronico())
                                .name(comprador.getNombreCompleto())
                                .build()
                )
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mpSuccessBackUrl)
                                .pending(mpPendingBackUrl)
                                .failure(mpFailureBackUrl)
                                .build()
                )
                .notificationUrl(publicUrl + "/api/mercadoPago/webhook/notification")
                .build();

        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .accessToken(tokenVendedor)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest, requestOptions);

        return preference;
    }

    public boolean handlePayment(Map<String, Object> body) throws MPException, MPApiException {

        System.out.println("Webhook body: " + body);

        Long paymentId;
        String type = String.valueOf(body.get("type"));
        Map<String, Object> data = (Map<String, Object>) body.get("data");

        if (!"payment".equals(type) || data == null || data.get("id") == null) {
            return false;
        }

        paymentId = Long.valueOf((String) data.get("id"));
        System.out.println("Procesando paymentId: " + paymentId);

        // 🔐 Usamos el token del app owner para obtener el Payment
        MPRequestOptions requestOptionsAppOwner = MPRequestOptions.builder()
                .accessToken(mpAccess) // tu token como owner
                .build();

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId, requestOptionsAppOwner);

        String externalReference = payment.getExternalReference();
        if (externalReference == null) {
            throw new RuntimeException("El payment no tiene external_reference");
        }

        Reserva reserva = reservaRepository.findById(Long.valueOf(externalReference))
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada para external_reference: " + externalReference));

        // Procesar el pago
        return procesarPago(payment, reserva);
    }

    public String createAuthorizationClient(Long ClienteId) throws MPException, MPApiException {
        Cliente cliente = clienteRepository.findById(ClienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        String tempId = UUID.randomUUID().toString();
        clientesTemporales.put(tempId, cliente);

        if(cliente.getAutorizaciones()==AutorizacionesCliente.MERCADO_PAGO||cliente.getAutorizaciones()==AutorizacionesCliente.AMBAS){
            throw new RuntimeException("El cliente ya tiene autorizaciones de Mercado Pago");
        }

            return buildAuthUrl(tempId);
    }

    public boolean getAuthorizationClient(String code, String state) throws MPException, MPApiException {

        String url = "https://api.mercadopago.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();

        // Body de la request
        Map<String, String> body = new HashMap<>();
        body.put("client_id", mpClientId);
        body.put("client_secret", mpClientSecret);
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        body.put("redirect_uri", publicUrl + "/api/mercadoPago/webhook/getAuthClient");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String accessToken = (String) response.getBody().get("access_token");
            String refreshToken = (String) response.getBody().get("refresh_token");
            Long userId = Long.valueOf(response.getBody().get("user_id").toString());

            Integer expiresIn = (Integer) response.getBody().get("expires_in");
            LocalDateTime tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn);

            Cliente cliente = clientesTemporales.remove(state);
            if (cliente == null) return false;

            try {
                CredencialesMP credenciales = CredencialesMP.builder()
                        .accessToken(cryptoUtils.encrypt(accessToken))
                        .refreshToken(cryptoUtils.encrypt(refreshToken))
                        .userIdMp(userId)
                        .tokenExpiration(tokenExpiration)
                        .build();
                cliente.setCredencialesMP(credenciales);
                clienteRepository.save(cliente);
                clienteService.actualizarAutorizaciones(cliente.getId());
            }catch (Exception e){
                throw new RuntimeException("Error al encriptar las credenciales" );
            }
            
            System.out.println(cliente);

            return true;
        }

        return false;
    }

    //Procesar el pago basado en su estado
    private boolean procesarPago(Payment payment, Reserva reserva) throws MPException, MPApiException {
        String status = payment.getStatus();

        if ("approved".equals(status)) {

            System.out.println("Procesando reserva: " + reserva);
            reservaService.cambiarEstado(reserva.getId(), Estado.RESERVADA);
            return true;

        } else if ("rejected".equals(status)) {
            reservaService.cambiarEstado(reserva.getId(), Estado.CANCELADA);
            return true;
        }

        return false;
    }

    private String buildAuthUrl(String tempId) throws MPException, MPApiException {
        String oauthClient = new OauthClient().getAuthorizationURL(mpClientId,
                publicUrl + "/api/mercadoPago/webhook/getAuthClient");
        oauthClient += "&state=" + tempId;
        return oauthClient;
    }

}