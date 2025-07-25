package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DireccionMapper;
import org.example.zarp_back.model.dto.direccion.DireccionDTO;
import org.example.zarp_back.model.dto.direccion.DireccionResponseDTO;
import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.repository.DireccionRepository;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends GenericoServiceImpl<Direccion, DireccionDTO, DireccionResponseDTO, Long> {

    public DireccionService(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        super(direccionRepository, direccionMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Dirección si es necesario
}
