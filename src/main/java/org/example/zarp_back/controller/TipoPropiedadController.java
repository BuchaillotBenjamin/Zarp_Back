package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.service.TipoPropiedadService;

public class TipoPropiedadController extends GenericoControllerImpl<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO, Long, TipoPropiedadService> {

    public TipoPropiedadController(TipoPropiedadService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de TipoPropiedad si es necesario
}
