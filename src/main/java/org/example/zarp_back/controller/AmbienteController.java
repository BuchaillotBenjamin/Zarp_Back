package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.ambiente.AmbienteDTO;
import org.example.zarp_back.model.dto.ambiente.AmbienteResponseDTO;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.service.AmbienteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController extends GenericoControllerImpl<Ambiente, AmbienteDTO, AmbienteResponseDTO, Long, AmbienteService> {


    public AmbienteController(AmbienteService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Ambiente si es necesario
}
