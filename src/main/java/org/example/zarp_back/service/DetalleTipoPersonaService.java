package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DetalleTipoPersonaMapper;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.DetalleTipoPersona;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.repository.DetalleTipoPersonaRepository;
import org.example.zarp_back.repository.TipoPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleTipoPersonaService extends GenericoServiceImpl<DetalleTipoPersona, DetalleTipoPersonaDTO, DetalleTipoPersonaResponseDTO, Long> {

    public DetalleTipoPersonaService(DetalleTipoPersonaRepository detalleTipoPersonaRepository, DetalleTipoPersonaMapper detalleTipoPersonaMapper) {
        super(detalleTipoPersonaRepository, detalleTipoPersonaMapper);
    }


    // Aquí puedes agregar métodos específicos para el servicio de DetalleTipoPersona si es necesario
}
