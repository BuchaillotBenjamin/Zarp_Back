package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.CaracteristicaMapper;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.repository.CaracteristicaRepository;
import org.example.zarp_back.repository.ImagenRepository;
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

    // 👇 Agregá este repo si no lo tenías
    @Autowired
    private ImagenRepository imagenRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository, CaracteristicaMapper caracteristicaMapper) {
        super(caracteristicaRepository, caracteristicaMapper);
    }

    @Override
    @Transactional
    public CaracteristicaResponseDTO update(Long id, CaracteristicaDTO dto) {
        // 1) Buscar existente
        Caracteristica existente = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Característica con el id " + id + " no encontrada"));

        boolean updated = false;

        // 2) Campos simples
        if (dto.getDenominacion() != null && !dto.getDenominacion().equals(existente.getDenominacion())) {
            existente.setDenominacion(dto.getDenominacion());
            updated = true;
        }

        if (dto.getDescripcion() != null && !dto.getDescripcion().equals(existente.getDescripcion())) {
            existente.setDescripcion(dto.getDescripcion());
            updated = true;
        }

        // 3) Imagen (clave del problema)
        if (dto.getImagen() != null && dto.getImagen().getUrlImagen() != null && !dto.getImagen().getUrlImagen().isBlank()) {
            String nuevaUrl = dto.getImagen().getUrlImagen();

            if (existente.getImagen() == null) {
                // No había imagen: crear y asignar
                Imagen nueva = new Imagen();
                nueva.setUrlImagen(nuevaUrl);
                imagenRepository.save(nueva);
                existente.setImagen(nueva);
                updated = true;
            } else {
                // Ya había imagen: actualizar su url
                Imagen img = existente.getImagen();
                if (!nuevaUrl.equals(img.getUrlImagen())) {
                    img.setUrlImagen(nuevaUrl);
                    imagenRepository.save(img);  // opcional si cascade=ALL, pero explícito es más claro
                    updated = true;
                }
            }
        }

        if (updated) {
            existente = caracteristicaRepository.save(existente);
        }

        return genericoMapper.toResponseDTO(existente);
    }

    public List<CaracteristicaResponseDTO> getActivos(Long propiedadId) {
        List<Caracteristica> caracteristicas = caracteristicaRepository.findByActivo(true);
        return caracteristicaMapper.toResponseDTOList(caracteristicas);
    }
}
