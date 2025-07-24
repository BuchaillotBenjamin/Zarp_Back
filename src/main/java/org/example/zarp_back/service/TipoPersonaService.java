package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.TipoPersonaMapper;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaDTO;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.repository.TipoPersonaRepository;

public class TipoPersonaService extends GenericoServiceImpl<TipoPersona, TipoPersonaDTO, TipoPersonaResponseDTO, Long> {

    public TipoPersonaService(TipoPersonaRepository tipoPersonaRepository, TipoPersonaMapper tipoPersonaMapper) {
        super(tipoPersonaRepository, tipoPersonaMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de TipoPersona si es necesario
}
