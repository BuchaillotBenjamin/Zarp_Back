package org.example.zarp_back.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
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
                                .email(cliente.getCorreoElectronico())
                                .name(cliente.getNombreCompleto())
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

        String type = String.valueOf(body.get("type"));
        Map<String, Object> data = (Map<String, Object>) body.get("data");

        if (!"payment".equals(type) || data == null || data.get("id") == null) {
            return false;
        }

        Long paymentId = Long.valueOf(String.valueOf(data.get("id")));

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId);

        String status = payment.getStatus();
        String externalReference = payment.getExternalReference();

        if ("approved".equals(status)) {
            // Recuperar la reserva temporal
            ReservaDTO reserva = reservasTemporales.remove(externalReference);

            if (reserva == null) return false;

            ReservaResponseDTO reservaEntity = reservaService.save(reserva);
            reservaService.cambiarEstado(reservaEntity.getId(), Estado.RESERVADA);


        } else if ("rejected".equals(status)) {
            // Si el pago fue rechazado, eliminar la reserva temporal
            reservasTemporales.remove(externalReference);
        }

        return true;
    }




}