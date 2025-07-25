package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.service.TipoPropiedadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipoPropiedades")
public class TipoPropiedadController extends GenericoControllerImpl<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO, Long, TipoPropiedadService> {

    public TipoPropiedadController(TipoPropiedadService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de TipoPropiedad si es necesario
}
