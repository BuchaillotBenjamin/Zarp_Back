package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DetalleCaracteristicaMapper;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.DetalleCaracteristica;
import org.example.zarp_back.repository.DetalleCaracteristicaRepository;
import org.springframework.stereotype.Service;

@Service
public class DetalleCaracteristicaService extends GenericoServiceImpl<DetalleCaracteristica, DetalleCaracteristicaDTO, DetalleCaracteristicaResponseDTO, Long> {

    public DetalleCaracteristicaService(DetalleCaracteristicaRepository detalleCaracteristicaRepository, DetalleCaracteristicaMapper detalleCaracteristicaMapper) {
        super(detalleCaracteristicaRepository, detalleCaracteristicaMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de DetalleCaracteristica si es necesario
}
