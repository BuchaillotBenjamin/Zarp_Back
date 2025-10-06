package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.repository.ReservaRepository;
import org.example.zarp_back.service.ReservaService;
import org.example.zarp_back.service.utils.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Slf4j
public class ReservaController extends GenericoControllerImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long, ReservaService> {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private NotificacionService notificacionService;


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

    @GetMapping("/test-notificacion")
    public ResponseEntity<String> testNotificacion() throws Exception {
        Reserva reserva = reservaRepository.findById(10L).orElseThrow();
        notificacionService.notificarReservaCliente(reserva);
        notificacionService.notificarReservaPropietario(reserva);
        return ResponseEntity.ok("Correo enviado");
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaResponseDTO>> getReservasByClienteId(@PathVariable Long clienteId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó reservas del cliente ID {}", uid, clienteId);

        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorClienteId(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/propiedad/{propiedadId}")
    public ResponseEntity<List<ReservaResponseDTO>> getReservasByPropiedadId(@PathVariable Long propiedadId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó reservas de la propiedad ID {}", uid, propiedadId);

        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorPropiedad(propiedadId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<List<List<ReservaResponseDTO>>> getReservasByPropietarioId(@PathVariable Long propietarioId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó reservas agrupadas por propietario ID {}", uid, propietarioId);

        List<List<ReservaResponseDTO>> reservas = reservaService.obeterReservasDePropietario(propietarioId);
        return ResponseEntity.ok(reservas);
    }




    // Aquí puedes agregar métodos específicos para el controlador de Reserva si es necesario
}
