package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController extends GenericoControllerImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long, ReservaService> {

    @Autowired
    ReservaService reservaService;

    @Override
    protected String entidadNombre() {
        return "reservas";
    }

    public ReservaController(ReservaService servicio) {
        super(servicio);
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<ReservaResponseDTO> finalizarReserva(@PathVariable Long id) {
        ReservaResponseDTO reservaResponseDTO = reservaService.reservaFinalizada(id);
        return ResponseEntity.ok(reservaResponseDTO);
    }


    // Aquí puedes agregar métodos específicos para el controlador de Reserva si es necesario
}
