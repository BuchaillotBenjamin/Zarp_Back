package org.example.zarp_back.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mercadoPago")
public class MercadoPagoController {

    @Autowired
    MercadoPagoService mercadoPagoService;

    @PostMapping("/create-preference")
    public ResponseEntity<String> createPreference(@Valid @RequestBody ReservaDTO reserva) throws MPException, MPApiException {
        Preference preference = mercadoPagoService.createPreference(reserva);
        return ResponseEntity.ok(preference.getInitPoint());
    }

    @PostMapping("/webhook/notification")
    public ResponseEntity<String> mercadoPagoWebhook(@RequestBody Map<String, Object> body)throws MPException, MPApiException {

        if (!mercadoPagoService.handlePayment(body)) {
            return ResponseEntity.badRequest().body("Error al procesar el webhook de Mercado Pago");
        }

        return ResponseEntity.status(200).body("Webhook de Mercado Pago recibido correctamente");

    }

    @PostMapping("/createAuthClient/{clienteId}")
    public ResponseEntity<String> createAuthClient(@PathVariable Long clienteId) throws MPException, MPApiException  {
        String authUrl = mercadoPagoService.createAuthorizationClient(clienteId);
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/webhook/getAuthClient")
    public ResponseEntity<Boolean> getAuthUrl(@RequestParam String code, @RequestParam String state) throws MPException, MPApiException {
        Boolean authUrl = mercadoPagoService.getAuthorizationClient(code, state);
        return ResponseEntity.ok(authUrl);
    }

}
