package org.example.zarp_back.controller;

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
public class VerificacionClienteController extends GenericoControllerImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long, VerificacionClienteService> {

    @Autowired
    private VerificacionClienteService verificacionClienteService;


    public VerificacionClienteController(VerificacionClienteService servicio) {
        super(servicio);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<VerificacionClienteResponseDTO>> getVerificacionesActivas() {

        return ResponseEntity.ok(verificacionClienteService.getVerificacionesActivas());

    }

    // Aquí puedes agregar métodos específicos para el controlador de VerificacionCliente si es necesario
}
