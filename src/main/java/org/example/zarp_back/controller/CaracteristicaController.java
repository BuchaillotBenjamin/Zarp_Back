package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.service.CaracteristicaService;

public class CaracteristicaController extends GenericoControllerImpl<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO, Long, CaracteristicaService> {

    public CaracteristicaController(CaracteristicaService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Característica si es necesario
}
