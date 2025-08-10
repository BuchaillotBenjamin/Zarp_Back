package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.CaracteristicaMapper;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.repository.CaracteristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaracteristicaService extends GenericoServiceImpl<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO, Long> {

    @Autowired
    private CaracteristicaMapper caracteristicaMapper;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository, CaracteristicaMapper caracteristicaMapper) {
        super(caracteristicaRepository, caracteristicaMapper);
    }

    @Override
    @Transactional
    public CaracteristicaResponseDTO update(Long id, CaracteristicaDTO caracteristicaDTO) {
        // Validar que la característica existe
        Caracteristica caracteristicaExistente = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Característica con el id " + id + " no encontrada"));

        boolean updated = false;

        if (!caracteristicaDTO.getDenominacion().equals(caracteristicaExistente.getDenominacion())) {
            caracteristicaExistente.setDenominacion(caracteristicaDTO.getDenominacion());
            updated = true;
        }

        if (!caracteristicaDTO.getDescripcion().equals(caracteristicaExistente.getDescripcion())) {
            caracteristicaExistente.setDescripcion(caracteristicaDTO.getDescripcion());
            updated = true;
        }

        if (updated) {
            caracteristicaRepository.save(caracteristicaExistente);
        }

        return genericoMapper.toResponseDTO(caracteristicaExistente);
    }

    public List<CaracteristicaResponseDTO> getActivos(Long propiedadId) {
        List<Caracteristica> caracteristicas = caracteristicaRepository.findByActivo(true);
        return caracteristicaMapper.toResponseDTOList(caracteristicas);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Característica si es necesario
}
