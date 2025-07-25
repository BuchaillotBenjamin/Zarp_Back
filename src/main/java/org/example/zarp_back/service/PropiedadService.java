package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.PropiedadMapper;
import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.repository.PropiedadRepository;
import org.springframework.stereotype.Service;

@Service
public class PropiedadService extends GenericoServiceImpl<Propiedad, PropiedadDTO, PropiedadResponseDTO, Long> {

    public PropiedadService(PropiedadRepository propiedadRepository, PropiedadMapper propiedadMapper) {
        super(propiedadRepository, propiedadMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Propiedad si es necesario
}
