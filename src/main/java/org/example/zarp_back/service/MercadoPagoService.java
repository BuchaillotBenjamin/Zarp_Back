package org.example.zarp_back.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PropiedadRepository propiedadRepository;
    @Autowired
    private ClienteRepository clienteRepository;


    private final String mpAccess="";
    private final String mpSuccessBackUrl="http://localhost:3000/reservas";
    private final String mpPendingBackUrl="http://localhost:3000/reservas";
    private final String mpFailureBackUrl="http://localhost:3000/reservas";
    private final String publicUrl="http://localhost:8080";


    // Mapa temporal para guardar los PedidoDTO por ID temporal
    private static final Map<String, ReservaDTO> reservasTemporales = new ConcurrentHashMap<>();

    @PostConstruct
    public void initMPConfig() {
        MercadoPagoConfig.setAccessToken(mpAccess);
    }

    public Preference createPreference(ReservaDTO reserva)throws MPException, MPApiException {

        Propiedad propiedad = propiedadRepository.findById(reserva.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada"));
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
        // Implementación para manejar el webhook de pago de MercadoPago
        return false;
    }




}
