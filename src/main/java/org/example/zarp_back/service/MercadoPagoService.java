package org.example.zarp_back.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mercadoPagoConfig.MercadoPagoConfig;
import org.example.zarp_back.model.dto.credencialesMP.CredencialesMPDTO;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.CredencialesMP;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.AutorizacionesCliente;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
    private PropiedadRepository propiedadRepository;
    @Autowired
    private MercadoPagoConfig mercadoPagoConfig;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private PagoPendienteService pagoPendienteService;
    @Autowired
    private ClienteService clienteService;

    // Mapa temporal
    private static final Map<String, ReservaDTO> reservasTemporales = new ConcurrentHashMap<>();


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

        /*String tokenVendedor;*/
        String tempId = UUID.randomUUID().toString();
        reservasTemporales.put(tempId, reserva);

        /*try {
            tokenVendedor = cryptoUtils.decrypt(vendedor.getCredencialesMP().getAccessToken());
        } catch (Exception e) {
            log.error("Error al desencriptar las credenciales de MP del vendedor ID {}: {}", vendedor.getId(), e.getMessage());
            throw new RuntimeException("Error al desencriptar las credenciales");
        }*/

        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Reserva temporal Zarp")
                .quantity(1)
                .unitPrice(new BigDecimal(reserva.getPrecioTotal()))
                .build();
        items.add(itemRequest);

        Cliente comprador = clienteRepository.findById(reserva.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
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
                .accessToken(mercadoPagoConfig.getMpAccess())
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

    //TODO: ARREGLAR ESTE METODO
    public boolean isValidWebhookSignature(String signatureHeader, String requestId, String dataId) {
        try {
            log.info("Validando firma del webhook con signatureHeader: {}, requestId: {}, dataId: {}", signatureHeader, requestId, dataId);
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

            if (ts == null || v1 == null || requestId == null || dataId == null) {
                log.warn("Faltan campos para construir el manifest: ts={}, v1={}, requestId={}, dataId={}", ts, v1, requestId, dataId);
                return false;
            }

            // Construir manifest en orden exacto
            String manifest = "id:" + dataId.toLowerCase() + ";request-id:" + requestId + ";ts:" + ts + ";";

            // Calcular HMAC-SHA256 en hexadecimal
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(mercadoPagoConfig.getMpSecretKeyWebhook().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = bytesToHex(hmacBytes);

            // 🔍 Logs de depuración
            log.warn("Manifest construido: {}", manifest);
            log.warn("Firma esperada: {}", expectedSignature);
            log.warn("Firma recibida: {}", v1);

            return expectedSignature.equalsIgnoreCase(v1);

        } catch (Exception e) {
            log.error("Error al validar firma del webhook", e);
            return false;
        }
    }

    public boolean guardarCuentaBancaria(Long clienteId, CredencialesMPDTO credencialesMP) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + clienteId));

        if(cliente.getRol() != Rol.PROPIETARIO ){
            log.info("El cliente ID: {} no tiene las verificaciones necesarias, no se pueden guardar credenciales de Mercado Pago", clienteId);
            return false;
        }

        if (cliente.getAutorizaciones() == AutorizacionesCliente.MERCADO_PAGO || cliente.getAutorizaciones() == AutorizacionesCliente.AMBAS) {
            log.info("El cliente ID: {} ya tiene autorización para Mercado Pago", clienteId);
            return false;
        }

        CredencialesMP credencialesMpEntity = CredencialesMP.builder()
                .cvu(credencialesMP.getCvu())
                .nombreTitular(credencialesMP.getNombreTitular())
                .build();

        cliente.setCredencialesMP(credencialesMpEntity);

        clienteRepository.save(cliente);
        clienteService.actualizarAutorizaciones(clienteId);
        log.info("Credenciales de Mercado Pago guardadas para el cliente ID: {}", clienteId);
        return true;
    }

    private boolean procesarPago(Payment payment, ReservaDTO reserva) throws MPException, MPApiException {
        String status = payment.getStatus();

        if ("approved".equals(status)) {
            ReservaResponseDTO reservaResponse = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaResponse.getId(), Estado.RESERVADA);
            log.info("Pago aprobado y reserva creada con ID: {}", reservaResponse.getId());
            pagoPendienteService.save(reservaResponse.getId());
            log.info("Pago pendiente creado para la reserva ID: {}", reservaResponse.getId());
            return true;
        } else if ("rejected".equals(status)) {
            ReservaResponseDTO reservaResponse = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaResponse.getId(), Estado.CANCELADA);
            log.warn("Pago rechazado y reserva cancelada con ID: {}", reservaResponse.getId());
            return true;
        }

        return false;
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