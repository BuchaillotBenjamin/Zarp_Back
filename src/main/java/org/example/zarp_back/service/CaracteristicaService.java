package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.CaracteristicaMapper;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.repository.CaracteristicaRepository;

public class CaracteristicaService extends GenericoServiceImpl<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO, Long> {

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository, CaracteristicaMapper caracteristicaMapper) {
        super(caracteristicaRepository, caracteristicaMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Característica si es necesario
}
