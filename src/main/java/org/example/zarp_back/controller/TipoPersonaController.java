package org.example.zarp_back.controller;

import org.apache.coyote.Response;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaDTO;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.service.TipoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipoPersona")
public class TipoPersonaController extends GenericoControllerImpl<TipoPersona, TipoPersonaDTO, TipoPersonaResponseDTO, Long, TipoPersonaService> {

    @Autowired
    TipoPersonaService servicio;

    public TipoPersonaController(TipoPersonaService servicio) {
        super(servicio);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoPersonaResponseDTO>> getActivos() {
        List<TipoPersonaResponseDTO> tipoPersonas = servicio.getActivos();
        return ResponseEntity.ok(tipoPersonas);
    }

    // Aquí puedes agregar métodos específicos para el controlador de TipoPersona si es necesario
}
