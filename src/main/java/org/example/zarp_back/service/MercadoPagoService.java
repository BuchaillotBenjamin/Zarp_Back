package org.example.zarp_back.service;

import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.oauth.CreateOauthCredential;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mercadoPagoConfig.MercadoPagoConfig;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.CredencialesMP;
import org.example.zarp_back.model.entity.Propiedad;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MercadoPagoService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private MercadoPagoConfig mercadoPagoConfig;
    @Autowired
    private CryptoUtils cryptoUtils;
    @Autowired
    private ReservaRepository reservaRepository;

    // Mapa temporal
    private static final Map<String, ReservaDTO> reservasTemporales = new ConcurrentHashMap<>();
    private static final Map<String, Cliente> clientesTemporales = new ConcurrentHashMap<>();


    public Preference createPreference(ReservaDTO reserva) throws MPException, MPApiException {

        Propiedad propiedad = propiedadRepository.findById(reserva.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada"));

        if (reservaRepository.findReservasSolapadas(reserva.getPropiedadId(), reserva.getFechaInicio(), reserva.getFechaFin()).size() > 0) {
            log.error("No se puede crear preferencia: la propiedad ID {} ya tiene reservas en las fechas {} - {}",
                    reserva.getPropiedadId(), reserva.getFechaInicio(), reserva.getFechaFin());
            throw new RuntimeException("No se puede crear preferencia: la propiedad ya tiene reservas solapadas en las fechas indicadas");
        }

        Cliente vendedor = propiedad.getPropietario();
        if (vendedor.getCredencialesMP() == null) {
            log.error("El vendedor ID {} no tiene credenciales de Mercado Pago", vendedor.getId());
            throw new RuntimeException("El vendedor no tiene credenciales de Mercado Pago");
        }

        String tokenVendedor;
        String tempId = UUID.randomUUID().toString();
        reservasTemporales.put(tempId, reserva);

        try {
            tokenVendedor = cryptoUtils.decrypt(vendedor.getCredencialesMP().getAccessToken());
        } catch (Exception e) {
            log.error("Error al desencriptar las credenciales de MP del vendedor ID {}: {}", vendedor.getId(), e.getMessage());
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
                .externalReference(String.valueOf(tempId))
                .payer(
                        PreferencePayerRequest.builder()
                                .email(comprador.getCorreoElectronico())
                                .name(comprador.getNombreCompleto())
                                .build()
                )
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mercadoPagoConfig.getMpSuccessBackUrl())
                                .pending(mercadoPagoConfig.getMpPendingBackUrl())
                                .failure(mercadoPagoConfig.getMpFailureBackUrl())
                                .build()
                )
                .notificationUrl(mercadoPagoConfig.getPublicUrl() + "/api/mercadoPago/webhook/notification")
                .build();

        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .accessToken(tokenVendedor)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest, requestOptions);

        log.info("Preference creada con ID: {}", preference.getId());
        return preference;
    }

    public boolean handlePayment(Map<String, Object> body) throws MPException, MPApiException {

        String type = String.valueOf(body.get("type"));
        Map<String, Object> data = (Map<String, Object>) body.get("data");

        if (!"payment".equals(type) || data == null || data.get("id") == null) {
            log.error("Notificación inválida recibida: {}", body);
            return false;
        }

        Long paymentId = Long.valueOf((String) data.get("id"));

        MPRequestOptions requestOptionsAppOwner = MPRequestOptions.builder()
                .accessToken(mercadoPagoConfig.getMpAccess())
                .build();

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId, requestOptionsAppOwner);

        String externalReference = payment.getExternalReference();
        if (externalReference == null) {
            log.error("El payment ID {} no tiene external_reference", paymentId);
            throw new RuntimeException("El payment no tiene external_reference");
        }

        ReservaDTO reserva = reservasTemporales.remove(externalReference);

        if (reserva == null) {
            log.info("Reserva ya procesada o no encontrada para externalReference: {}. Ignorando pago ID {}", externalReference, paymentId);
            return true;
        }

        log.info("Procesando pago ID: {} con estado: {}", paymentId, payment.getStatus());

        boolean exito = procesarPago(payment, reserva);
        if (exito) {
            log.info("Pago ID {} procesado exitosamente", paymentId);
        } else {
            log.warn("El pago ID {} no fue aprobado ni rechazado. Estado: {}", paymentId, payment.getStatus());
        }

        return exito;
    }

    public URI createAuthorizationClient(Long ClienteId) throws MPException, MPApiException {
        Cliente cliente = clienteRepository.findById(ClienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        String tempId = UUID.randomUUID().toString();
        clientesTemporales.put(tempId, cliente);

        if(cliente.getAutorizaciones()==AutorizacionesCliente.MERCADO_PAGO||cliente.getAutorizaciones()==AutorizacionesCliente.AMBAS){
            log.error("El cliente ID {} ya tiene autorizaciones de Mercado Pago", cliente.getId());
            throw new RuntimeException("El cliente ya tiene autorizaciones de Mercado Pago");
        }
            log.info("Generando URL de autorización para cliente ID: {}", cliente.getId());

        URI authUrl = buildAuthUrl(tempId);
            return authUrl;
    }

    @Transactional
    public boolean getAuthorizationClient(String code, String state) throws MPException, MPApiException {

        String url = "https://api.mercadopago.com/oauth/token";
        log.info("Intercambiando código por tokens en URL: {}", url);

        RestTemplate restTemplate = new RestTemplate();

        // Body de la request
        Map<String, String> body = new HashMap<>();
        body.put("client_id", mercadoPagoConfig.getMpClientId());
        body.put("client_secret", mercadoPagoConfig.getMpClientSecret());
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        body.put("redirect_uri", mercadoPagoConfig.getPublicUrl() + "/api/mercadoPago/webhook/getAuthClient");

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
                log.error("Error al encriptar las credenciales de MP para el cliente ID {}: {}", cliente.getId(), e.getMessage());
                throw new RuntimeException("Error al encriptar las credenciales" );
            }

            log.info("Credenciales de Mercado Pago guardadas para cliente ID: {}", cliente.getId());
            return true;
        }

        return false;
    }

    @Transactional
    public void refrescarToken(Cliente cliente) {
        CredencialesMP cred = cliente.getCredencialesMP();
        if (cred == null) return;

        OauthClient oauthClient = new OauthClient();

        try {
            CreateOauthCredential newCreds = oauthClient.createCredential(cryptoUtils.decrypt(cred.getRefreshToken()), null);

            LocalDateTime tokenExpiration = LocalDateTime.now().plusSeconds(newCreds.getExpiresIn());
            // Crear una nueva entidad para evitar conflictos
            CredencialesMP nuevaCred = new CredencialesMP();
            nuevaCred.setAccessToken(cryptoUtils.encrypt(newCreds.getAccessToken()));
            nuevaCred.setRefreshToken(cryptoUtils.encrypt(newCreds.getRefreshToken()));
            nuevaCred.setTokenExpiration(tokenExpiration);

            // Asignar la nueva credencial al cliente
            cliente.setCredencialesMP(nuevaCred);

            // Guardar el cliente con la nueva credencial
            clienteRepository.save(cliente);

            log.info("Token renovado para cliente ID: {}", cliente.getId());

        } catch (Exception e) {
            log.error("Error al refrescar token para cliente ID {}: {}", cliente.getId(), e.getMessage());
            throw new RuntimeException("Error al refrescar token de Mercado Pago");
        }
    }

    public boolean isValidWebhookSignature(String signatureHeader, String requestId, String dataId) {
        try {
            if (signatureHeader == null || mercadoPagoConfig.getMpSecretKeyWebhook() == null) {
                log.warn("Faltan headers o clave secreta");
                return false;
            }

            // Extraer ts y v1
            String ts = null;
            String v1 = null;
            for (String part : signatureHeader.split(",")) {
                String[] kv = part.split("=");
                if (kv.length == 2) {
                    String key = kv[0].trim();
                    String value = kv[1].trim();
                    if ("ts".equals(key)) ts = value;
                    else if ("v1".equals(key)) v1 = value;
                }
            }

            if (ts == null || v1 == null) {
                log.warn("No se encontró ts o v1 en x-signature");
                return false;
            }

            // Construir manifest
            StringBuilder manifest = new StringBuilder();
            if (dataId != null) manifest.append("id:").append(dataId.toLowerCase()).append(";");
            if (requestId != null) manifest.append("request-id:").append(requestId).append(";");
            manifest.append("ts:").append(ts).append(";");

            // Calcular HMAC-SHA256 en hexadecimal
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(mercadoPagoConfig.getMpSecretKeyWebhook().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(manifest.toString().getBytes(StandardCharsets.UTF_8));
            String expectedSignature = bytesToHex(hmacBytes);

            return MessageDigest.isEqual(expectedSignature.getBytes(), v1.getBytes());

        } catch (Exception e) {
            log.error("Error al validar firma del webhook: {}", e.getMessage());
            return false;
        }
    }

    private boolean procesarPago(Payment payment, ReservaDTO reserva) throws MPException, MPApiException {
        String status = payment.getStatus();

        if ("approved".equals(status)) {
            ReservaResponseDTO reservaResponse = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaResponse.getId(), Estado.RESERVADA);
            log.info("Pago aprobado y reserva creada con ID: {}", reservaResponse.getId());
            return true;
        } else if ("rejected".equals(status)) {
            ReservaResponseDTO reservaResponse = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaResponse.getId(), Estado.CANCELADA);
            log.warn("Pago rechazado y reserva cancelada con ID: {}", reservaResponse.getId());
            return true;
        }

        return false;
    }

    /*private String buildAuthUrl(String tempId) throws MPException, MPApiException {
        String redirectUrl = publicUrl + "/api/mercadoPago/webhook/getAuthClient";
        log.info("redirectURL {}",redirectUrl );
        OauthClient oauthClient = new OauthClient();
        try {String url = oauthClient.getAuthorizationURL(mpClientId, redirectUrl);
        url += "&state=" + tempId;
        return url;}
        catch (MPApiException e){
            log.error("MercadoPago API error: {}", e.getApiResponse().getContent());
            throw new RuntimeException("Error al construir la URL de autorización: " + e.getApiResponse().getContent());
        }
    }*/

    private URI buildAuthUrl(String tempId) throws MPException, MPApiException {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        String codeMethod = "S256";

        System.out.println("codeVerifier: " + codeVerifier);
        System.out.println("codeChallenge: " + codeChallenge);
        String baseUrl="";

        // Usar el SDK oficial para obtener la URL base
        try {
            baseUrl = new OauthClient().getAuthorizationURL(
                    mercadoPagoConfig.getMpClientId(),
                    mercadoPagoConfig.getPublicUrl() + "/api/mercadoPago/webhook/getAuthClient",
                    MPRequestOptions.createDefault()
            );
        }catch (MPApiException e) {
            log.error("MercadoPago API error: {}", e.getApiResponse().getContent());
            throw e; // o manejarlo como prefieras
        } catch (Exception e) {
            log.error("Error inesperado al obtener la URL de autorización: {}", e.getMessage());
            throw e;
        }



        // Agregar parámetros PKCE y estado
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", codeMethod)
                .queryParam("state", tempId)
                .build(true)
                .toUri();
    }

    private String generateCodeVerifier() {
        byte[] code = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar code_challenge", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}