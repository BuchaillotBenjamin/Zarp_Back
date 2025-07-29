package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.TipoPersonaMapper;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaDTO;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.repository.TipoPersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoPersonaService extends GenericoServiceImpl<TipoPersona, TipoPersonaDTO, TipoPersonaResponseDTO, Long> {

    @Autowired
    private TipoPersonaMapper tipoPersonaMapper;
    @Autowired
    private TipoPersonaRepository tipoPersonaRepository;

    public TipoPersonaService(TipoPersonaRepository tipoPersonaRepository, TipoPersonaMapper tipoPersonaMapper) {
        super(tipoPersonaRepository, tipoPersonaMapper);
    }

    @Override
    @Transactional
    public TipoPersonaResponseDTO update(Long id, TipoPersonaDTO tipoPersonaDTO) {
        TipoPersona tipoPersona = tipoPersonaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TipoPersona con el id " + id + " no encontrado"));

        boolean updated = false;

        if (!tipoPersonaDTO.getDenominacion().equals(tipoPersona.getDenominacion())) {
            tipoPersona.setDenominacion(tipoPersonaDTO.getDenominacion());
            updated = true;
        }

        if (!tipoPersonaDTO.getDescripcion().equals(tipoPersona.getDescripcion())) {
            tipoPersona.setDescripcion(tipoPersonaDTO.getDescripcion());
            updated = true;
        }

        if (updated){
            tipoPersonaRepository.save(tipoPersona);
        }

        return tipoPersonaMapper.toResponseDTO(tipoPersona);
    }

    // Aquí puedes agregar métodos específicos para el servicio de TipoPersona si es necesario
}
