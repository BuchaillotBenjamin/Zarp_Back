package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.service.VerificacionClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/verificacionClientes")
@Slf4j
public class VerificacionClienteController extends GenericoControllerImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long, VerificacionClienteService> {

    @Autowired
    private VerificacionClienteService verificacionClienteService;

    @Override
    protected String entidadNombre() {
        return "verificacionClientes";
    }

    public VerificacionClienteController(VerificacionClienteService servicio) {
        super(servicio);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<VerificacionClienteResponseDTO>> getVerificacionesActivas(HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó verificaciones activas de clientes", uid);

        List<VerificacionClienteResponseDTO> verificaciones = verificacionClienteService.getVerificacionesActivas();
        return ResponseEntity.ok(verificaciones);
    }



}
