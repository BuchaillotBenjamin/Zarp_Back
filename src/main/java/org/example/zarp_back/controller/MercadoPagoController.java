package org.example.zarp_back.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.service.AuditoriaService;
import org.example.zarp_back.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mercadoPago")
@Slf4j
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;
    @Autowired
    private AuditoriaService auditoriaService;

    @PostMapping("/create-preference")
    public ResponseEntity<String> createPreference(@Valid @RequestBody ReservaDTO reserva, HttpServletRequest request) throws MPException, MPApiException {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} inició la creación de una preferencia de pago para la reserva", uid);

        Preference preference = mercadoPagoService.createPreference(reserva);
        auditoriaService.registrar(uid, "Reserva", "Crear preferencia de pago", "N/A");
        return ResponseEntity.ok(preference.getInitPoint());
    }

    /*@PostMapping("/webhook/notification")
    public ResponseEntity<String> mercadoPagoWebhook(@RequestBody Map<String, Object> body)throws MPException, MPApiException {

        if (!mercadoPagoService.handlePayment(body)) {
            return ResponseEntity.badRequest().body("Error al procesar el webhook de Mercado Pago");
        }

        return ResponseEntity.status(200).body("Webhook de Mercado Pago recibido correctamente");

    }*/

    @PostMapping("/webhook/notification")
    public ResponseEntity<String> mercadoPagoWebhook(HttpServletRequest request) {
        try {
            String signatureHeader = request.getHeader("x-signature");
            String requestId = request.getHeader("x-request-id");
            String dataId = request.getParameter("data.id");

            String rawBody = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            if (!mercadoPagoService.isValidWebhookSignature(signatureHeader, requestId, dataId)) {
                log.warn("Firma inválida en webhook. Rechazado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Firma inválida");
            }

            log.info("Webhook de Mercado Pago validado correctamente");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> body = mapper.readValue(rawBody, new TypeReference<Map<String, Object>>() {
            });

            if (!mercadoPagoService.handlePayment(body)) {
                return ResponseEntity.badRequest().body("Error al procesar el webhook de Mercado Pago");
            }

            return ResponseEntity.ok("Webhook de Mercado Pago recibido correctamente");

        } catch (Exception e) {
            log.error("Error procesando webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    @PostMapping("/createAuthClient/{clienteId}")
    public ResponseEntity<String> createAuthClient(@PathVariable Long clienteId, HttpServletRequest request) throws MPException, MPApiException {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} inició autorización de cliente ID {}", uid, clienteId);

        String authUrl = mercadoPagoService.createAuthorizationClient(clienteId);
        auditoriaService.registrar(uid, "Cliente", "Iniciar autorización Mercado Pago", clienteId.toString());
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/webhook/getAuthClient")
    public ResponseEntity<Boolean> getAuthUrl(@RequestParam String code, @RequestParam String state) throws MPException, MPApiException {
        Boolean authUrl = mercadoPagoService.getAuthorizationClient(code, state);
        return ResponseEntity.ok(authUrl);
    }

}
