package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.dto.reserva.ReservaFechaDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.Provincia;
import org.example.zarp_back.service.AuditoriaService;
import org.example.zarp_back.service.PropiedadService;
import org.example.zarp_back.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@Slf4j
public class PropiedadController extends GenericoControllerImpl<Propiedad, PropiedadDTO, PropiedadResponseDTO, Long, PropiedadService> {

    @Autowired
    private PropiedadService propiedadService;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AuditoriaService auditoriaService;

    @Override
    protected String entidadNombre() {
        return "propiedades";
    }

    public PropiedadController(PropiedadService servicio) {
        super(servicio);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesByCliente(@PathVariable Long idCliente, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó propiedades del cliente ID {}", uid, idCliente);

        List<PropiedadResponseDTO> propiedades = propiedadService.getPropiedadesByCliente(idCliente);
        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/provincias")
    public ResponseEntity<List<List<PropiedadResponseDTO>>> getPropiedadByProvincia(HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó propiedades agrupadas por provincia", uid);

        List<List<PropiedadResponseDTO>> propiedadesPorProvincia = propiedadService.getPropiedadesByProvincia();
        return ResponseEntity.ok(propiedadesPorProvincia);
    }

    @GetMapping("/provicias/{provincia}")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesByProvincia(@PathVariable Provincia provincia, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó propiedades en la provincia {}", uid, provincia);

        List<PropiedadResponseDTO> propiedades = propiedadService.getPropiedadesByProvincia(provincia);
        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/reservas/{propiedadId}")
    public ResponseEntity<List<ReservaFechaDTO>> getReservasActivasPorPropiedad(@PathVariable Long propiedadId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó reservas activas para propiedad ID {}", uid, propiedadId);

        List<ReservaFechaDTO> reservas = reservaService.fechasReservadas(propiedadId);
        return ResponseEntity.ok(reservas);
    }

    @PutMapping("/verificacion/{id}")
    public ResponseEntity<PropiedadResponseDTO> verificacionPropiedad(@PathVariable Long id,
                                                                      @RequestParam boolean activar,
                                                                      HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} modificó verificación de propiedad ID {} a estado {}", uid, id, activar);

        PropiedadResponseDTO propiedadActualizada = propiedadService.verificarPropiedad(id, activar);

        messagingTemplate.convertAndSend("/topic/propiedades/update", propiedadActualizada);

        auditoriaService.registrar(uid, "Propiedad", activar ? "Aprobar verificación" : "Rechazar verificación", propiedadActualizada.toString());
        return ResponseEntity.ok(propiedadActualizada);
    }

    @GetMapping("/aVerificar")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesPorVerificar(HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó propiedades pendientes de verificación", uid);

        List<PropiedadResponseDTO> propiedades = propiedadService.propiedadesVerificar();
        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesActivas(HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} solicitó propiedades activas y verificadas", uid);

        List<PropiedadResponseDTO> propiedades = propiedadService.getActivasVerificadas();
        return ResponseEntity.ok(propiedades);
    }




    // Aquí puedes agregar métodos específicos para el controlador de Propiedad si es necesario
}
