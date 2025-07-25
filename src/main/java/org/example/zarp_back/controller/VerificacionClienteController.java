package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.service.VerificacionClienteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verificacionClientes")
public class VerificacionClienteController extends GenericoControllerImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long, VerificacionClienteService> {

    public VerificacionClienteController(VerificacionClienteService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de VerificacionCliente si es necesario
}
