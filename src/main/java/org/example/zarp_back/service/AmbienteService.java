package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.AmbienteMapper;
import org.example.zarp_back.model.dto.ambiente.AmbienteDTO;
import org.example.zarp_back.model.dto.ambiente.AmbienteResponseDTO;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.repository.AmbienteRepository;
import org.springframework.stereotype.Service;

@Service
public class AmbienteService extends GenericoServiceImpl<Ambiente, AmbienteDTO, AmbienteResponseDTO, Long> {

    public AmbienteService(AmbienteRepository ambienteRepository, AmbienteMapper ambienteMapper) {
        super(ambienteRepository, ambienteMapper);
    }


}
