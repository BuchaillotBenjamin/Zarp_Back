package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.TipoPropiedadMapper;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.repository.TipoPropiedadRepository;

public class TipoPropiedadService extends GenericoServiceImpl<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO, Long> {

    public TipoPropiedadService(TipoPropiedadRepository tipoPropiedadRepository, TipoPropiedadMapper tipoPropiedadMapper) {
        super(tipoPropiedadRepository, tipoPropiedadMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de TipoPropiedad si es necesario
}
