package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.service.PropiedadService;

public class PropiedadController extends GenericoControllerImpl<Propiedad, PropiedadDTO, PropiedadResponseDTO, Long, PropiedadService> {

    public PropiedadController(PropiedadService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Propiedad si es necesario
}
