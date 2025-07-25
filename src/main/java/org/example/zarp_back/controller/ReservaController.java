package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.service.ReservaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController extends GenericoControllerImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long, ReservaService> {

    public ReservaController(ReservaService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Reserva si es necesario
}
