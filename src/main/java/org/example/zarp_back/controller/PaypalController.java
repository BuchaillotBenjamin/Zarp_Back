package org.example.zarp_back.controller;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador que expone los endpoints para operaciones de orden / captura
 * en el flujo multiparty de PayPal.
 */
@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PaypalController {

    @Autowired
    private PaypalService paypalService;

   /* @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody ReservaDTO reserva) {

        String order = paypalService.createPayout(reserva);
        return ResponseEntity.ok(order);

    }*/

    @PostMapping("/webhook/getpayout")
    public ResponseEntity<String> getPayout(@RequestBody Map<String, Object> payload) {

        log.info("Webhook recibido: {}", payload);
        Boolean exito =paypalService.procesarWebhookPayout(payload);

        if (!exito) {
            return ResponseEntity.badRequest().body("Error al procesar el webhook");
        }
        return ResponseEntity.ok().build();

    }

    @PutMapping("/guardarDireccionPaypal/{clienteId}")
    public ResponseEntity<String> captureOrder(@PathVariable Long clienteId, @RequestBody String direccionPaypal) {
        Boolean status = paypalService.guardarDireccionPaypal(clienteId, direccionPaypal);
        return ResponseEntity.ok("Guardado con exito: " + status);
    }

    @PostMapping("/crearOrdenPago")
    public ResponseEntity<String> crearOrdenPago(@RequestBody ReservaDTO reserva) {
        String ordenId = paypalService.createPayPalOrder(reserva);
        return ResponseEntity.ok(ordenId);
    }

    @PostMapping("/webhook/getOrdenPago")
    public ResponseEntity<String> getOrdenPago(@RequestBody Map<String, Object> payload) {

        log.info("Webhook recibido: {}", payload);
        Boolean exito =paypalService.procesarWebhookOrdenPago(payload);

        if (!exito) {
            log.error("Error al procesar el webhook de orden de pago");
            return ResponseEntity.ok().body("Error al procesar el webhook");
        }
        return ResponseEntity.ok().build();

    }


}
