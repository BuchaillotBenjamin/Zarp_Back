package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaDTO;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.service.TipoPersonaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipoPersona")
public class TipoPersonaController extends GenericoControllerImpl<TipoPersona, TipoPersonaDTO, TipoPersonaResponseDTO, Long, TipoPersonaService> {


    public TipoPersonaController(TipoPersonaService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de TipoPersona si es necesario
}
