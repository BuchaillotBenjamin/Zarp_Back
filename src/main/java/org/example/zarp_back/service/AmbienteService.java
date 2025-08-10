package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.AmbienteMapper;
import org.example.zarp_back.model.dto.ambiente.AmbienteDTO;
import org.example.zarp_back.model.dto.ambiente.AmbienteResponseDTO;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.repository.AmbienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AmbienteService extends GenericoServiceImpl<Ambiente, AmbienteDTO, AmbienteResponseDTO, Long> {

    @Autowired
    private AmbienteMapper ambienteMapper;
    @Autowired
    private AmbienteRepository ambienteRepository;


    public AmbienteService(AmbienteRepository ambienteRepository, AmbienteMapper ambienteMapper) {
        super(ambienteRepository, ambienteMapper);
    }

    @Override
    @Transactional
    public AmbienteResponseDTO update(Long id, AmbienteDTO ambienteDTO) {
        // Validar que el ambiente existe
        Ambiente ambienteExistente = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ambiente con el id " + id + " no encontrado"));

        if (!ambienteDTO.getDenominacion().equals(ambienteExistente.getDenominacion())) {
            ambienteExistente.setDenominacion(ambienteDTO.getDenominacion());
            ambienteRepository.save(ambienteExistente);
        }

        return genericoMapper.toResponseDTO(ambienteExistente);
    }

    public List<AmbienteResponseDTO> getActivos(){
        List<Ambiente> ambientesActivos = ambienteRepository.findByActivo(true);
        if (ambientesActivos.isEmpty()) {
            throw new NotFoundException("No se encontraron ambientes activos");
        }
        return ambienteMapper.toResponseDTOList(ambientesActivos);
    }

}
