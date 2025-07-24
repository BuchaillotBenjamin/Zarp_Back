package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DetalleAmbienteMapper;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteResponseDTO;
import org.example.zarp_back.model.entity.DetalleAmbiente;
import org.example.zarp_back.repository.DetalleAmbienteRepository;

public class DetalleAmbienteService extends GenericoServiceImpl<DetalleAmbiente, DetalleAmbienteDTO, DetalleAmbienteResponseDTO, Long> {

    public DetalleAmbienteService(DetalleAmbienteRepository detalleAmbienteRepository, DetalleAmbienteMapper detalleAmbienteMapper) {
        super(detalleAmbienteRepository, detalleAmbienteMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de DetalleAmbiente si es necesario
}
