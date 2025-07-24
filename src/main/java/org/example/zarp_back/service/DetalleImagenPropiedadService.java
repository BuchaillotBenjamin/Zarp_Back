package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DetalleImagenPropiedadMapper;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadResponseDTO;
import org.example.zarp_back.model.entity.DetalleImagenPropiedad;
import org.example.zarp_back.repository.DetalleImagenPropiedadRepository;

public class DetalleImagenPropiedadService extends GenericoServiceImpl<DetalleImagenPropiedad, DetalleImagenPropiedadDTO, DetalleImagenPropiedadResponseDTO, Long> {

    public DetalleImagenPropiedadService(DetalleImagenPropiedadRepository detalleImagenPropiedadRepository, DetalleImagenPropiedadMapper detalleImagenPropiedadMapper) {
        super(detalleImagenPropiedadRepository, detalleImagenPropiedadMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de DetalleImagenPropiedad si es necesario
}
