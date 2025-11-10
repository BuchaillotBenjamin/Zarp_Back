package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.pagosPendientes.PagoPendienteResponseDTO;
import org.example.zarp_back.model.enums.EstadoPagosPendientes;
import org.example.zarp_back.service.PagoPendienteService;
import org.example.zarp_back.service.utils.WebSocketsNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagosPendientes")
public class PagoPendienteController {


    @Autowired
    private PagoPendienteService pagoPendienteService;
    @Autowired
    private WebSocketsNotificacion webSocketsNotificacion;
    private final String entidadNombre = "pagosPendientes";

    @GetMapping("/activos")
    public ResponseEntity<List<PagoPendienteResponseDTO>> getActivos(){
        List<PagoPendienteResponseDTO> pagosPendientes = pagoPendienteService.findActivos();
        return ResponseEntity.ok(pagosPendientes);
    }

    @PatchMapping("/toggleActivo/{id}")
    public ResponseEntity<PagoPendienteResponseDTO> toggleActivo(@PathVariable Long id){
        PagoPendienteResponseDTO pagoPendiente = pagoPendienteService.toggleActivo(id);
        webSocketsNotificacion.NotificarUpdate(entidadNombre, pagoPendiente);
        return ResponseEntity.ok(pagoPendiente);
    }

    @PatchMapping("/iniciar/{id}")
    public ResponseEntity<PagoPendienteResponseDTO> iniciarPago(@PathVariable Long id, @RequestParam String uidEmpleado){
        PagoPendienteResponseDTO pagoPendiente = pagoPendienteService.iniciarPago(id, uidEmpleado);
        webSocketsNotificacion.NotificarUpdate(entidadNombre, pagoPendiente);
        return ResponseEntity.ok(pagoPendiente);
    }

    @PatchMapping("/cambiarEstado/{id}")
    public ResponseEntity<PagoPendienteResponseDTO> cambiarEstado(@PathVariable Long id){
        PagoPendienteResponseDTO pagoPendiente = pagoPendienteService.cambiarEstado(id);
        webSocketsNotificacion.NotificarUpdate(entidadNombre, pagoPendiente);
        return ResponseEntity.ok(pagoPendiente);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PagoPendienteResponseDTO> getById(@PathVariable Long id){
        PagoPendienteResponseDTO pagoPendiente = pagoPendienteService.getById(id);
        return ResponseEntity.ok(pagoPendiente);
    }

    @GetMapping("getByEstado/{estado}")
    public ResponseEntity<List<PagoPendienteResponseDTO>> getByEstado(@PathVariable EstadoPagosPendientes estado){
        List<PagoPendienteResponseDTO> pagosPendientes = pagoPendienteService.getByEstado(estado);
        return ResponseEntity.ok(pagosPendientes);
    }

    //TODO: GET BY ESTADO

    //PARA PRUEBAS
    @PostMapping("/save/{reservaID}")
    public ResponseEntity<String> save(@PathVariable Long reservaID){
        pagoPendienteService.save(reservaID);
        return ResponseEntity.ok().body("Pago pendiente creado para la reserva con ID: " + reservaID);
    }



}
