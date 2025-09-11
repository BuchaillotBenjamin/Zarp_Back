package org.example.zarp_back.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mercadoPago")
public class MercadoPagoController {

    @Autowired
    MercadoPagoService mercadoPagoService;

    @PostMapping("/create-preference")
    public ResponseEntity<Preference> createPreference(@Valid @RequestBody ReservaDTO reserva) throws MPException, MPApiException {
        Preference preference = mercadoPagoService.createPreference(reserva);
        return ResponseEntity.ok(preference);
    }

    @PostMapping("/webhook/notification")
    public ResponseEntity<String> mercadoPagoWebhook(@RequestBody Map<String, Object> body)throws MPException, MPApiException {
        if (!mercadoPagoService.handlePayment(body)) {
            return ResponseEntity.badRequest().body("Error al procesar el webhook de Mercado Pago.");
        }
        return ResponseEntity.ok("Webhook de Mercado Pago recibido correctamente");
    }



}
