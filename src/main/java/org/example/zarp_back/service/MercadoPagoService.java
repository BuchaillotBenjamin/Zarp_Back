package org.example.zarp_back.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.merchantorder.MerchantOrderPayment;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MercadoPagoService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaService reservaService;

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


    // Mapa temporal
    private static final Map<String, ReservaDTO> reservasTemporales = new ConcurrentHashMap<>();

    @PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccess);
    }

    public Preference createPreference(ReservaDTO reserva)throws MPException, MPApiException {
        Cliente cliente = clienteRepository.findById(reserva.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        // Generar un ID temporal único
        String tempId = UUID.randomUUID().toString();
        reservasTemporales.put(tempId, reserva);

        // Crear items para la preferencia
        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Reserva #" + tempId)
                .quantity(1)
                .unitPrice(new BigDecimal(reserva.getPrecioTotal()))
                .build();
        items.add(itemRequest);

        // Construir la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(
                        PreferencePayerRequest.builder()
                                .email("")
                                .name("")
                                .build()
                )
                .backUrls(
                        PreferenceBackUrlsRequest.builder()
                                .success(mpSuccessBackUrl)
                                .pending(mpPendingBackUrl)
                                .failure(mpFailureBackUrl)
                                .build()
                )
                .externalReference(tempId)
                .notificationUrl(publicUrl + "/api/mercadoPago/webhook/notification")
                .build();

        // Crear la preferencia
        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }

    public boolean handlePayment(Map<String, Object> body) throws MPException, MPApiException {

        System.out.println("Webhook body: " + body);

        Long paymentId;
        String type = String.valueOf(body.get("type"));
        Map<String, Object> data = (Map<String, Object>) body.get("data");

        if (type.equals("payment") && (data != null && data.get("id") != null)) {
            paymentId = Long.valueOf((String) data.get("id"));
            System.out.println("Procesando paymentId: " + paymentId);
        } else {
            return false;
        }

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId);

        return procesarPago(payment);
    }

    // Procesamiento de los pagos
    private boolean procesarPago(Payment payment) throws MPException, MPApiException {
        String status = payment.getStatus();
        String externalReference = payment.getExternalReference();
        System.out.println("Procesando pago con status: " + status + " y externalReference: " + externalReference);

        if ("approved".equals(status)) {
            // Recuperar la reserva temporal
            ReservaDTO reserva = reservasTemporales.remove(externalReference);
            if (reserva == null) return false;

            System.out.println("Procesando reserva: " + reserva);
            ReservaResponseDTO reservaEntity = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaEntity.getId(), Estado.RESERVADA);
            return true;

        } else if ("rejected".equals(status)) {
            System.out.println("Pago rechazado para externalReference: " + externalReference);
            // Si el pago fue rechazado, eliminar la reserva temporal
            reservasTemporales.remove(externalReference);
            return true;
        }

        return false;
    }

}