package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.repository.ReservaRepository;
import org.example.zarp_back.service.ReservaService;
import org.example.zarp_back.service.utils.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController extends GenericoControllerImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long, ReservaService> {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private NotificacionService notificacionService;


    public ReservaController(ReservaService servicio) {
        super(servicio);
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<ReservaResponseDTO> finalizarReserva(@PathVariable Long id) {
        ReservaResponseDTO reservaResponseDTO = reservaService.reservaFinalizada(id);
        return ResponseEntity.ok(reservaResponseDTO);
    }

    @GetMapping("/test-notificacion")
    public ResponseEntity<String> testNotificacion() throws Exception {
        Reserva reserva = reservaRepository.findById(10L).orElseThrow();
        notificacionService.notificarReservaCliente(reserva);
        notificacionService.notificarReservaPropietario(reserva);
        return ResponseEntity.ok("Correo enviado");
    }

    // Aquí puedes agregar métodos específicos para el controlador de Reserva si es necesario
}
