package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.dto.reserva.ReservaFechaDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.Provincia;
import org.example.zarp_back.service.PropiedadService;
import org.example.zarp_back.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
public class PropiedadController extends GenericoControllerImpl<Propiedad, PropiedadDTO, PropiedadResponseDTO, Long, PropiedadService> {

    @Autowired
    PropiedadService propiedadService;
    @Autowired
    ReservaService reservaService;

    public PropiedadController(PropiedadService servicio) {
        super(servicio);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesByCliente(@PathVariable Long idCliente) {
        List<PropiedadResponseDTO> propiedades = propiedadService.getPropiedadesByCliente(idCliente);
        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/provincias")
    public ResponseEntity<List<List<PropiedadResponseDTO>>> getPropiedadByProvincia(){

        List<List<PropiedadResponseDTO>> propiedadesPorProvincia = propiedadService.getPropiedadesByProvincia();
        return ResponseEntity.ok(propiedadesPorProvincia);

    }

    @GetMapping("/provicias/{provincia}")
    public ResponseEntity<List<PropiedadResponseDTO>> getPropiedadesByProvincia(@PathVariable Provincia provincia) {

        List<PropiedadResponseDTO> propiedades = propiedadService.getPropiedadesByProvincia(provincia);
        return ResponseEntity.ok(propiedades);

    }

    @GetMapping("/reservas/{propiedadId}")
    public ResponseEntity<List<?>> getReservasActivasPorPropiedad(@PathVariable Long propiedadId) {
        List<ReservaFechaDTO> reservas = reservaService.fechasReservadas(propiedadId);
        return ResponseEntity.ok(reservas);
    }


    // Aquí puedes agregar métodos específicos para el controlador de Propiedad si es necesario
}
