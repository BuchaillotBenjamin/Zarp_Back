package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.TipoPropiedadMapper;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.repository.TipoPropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoPropiedadService extends GenericoServiceImpl<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO, Long> {

    @Autowired
    private TipoPropiedadRepository tipoPropiedadRepository;
    @Autowired
    private TipoPropiedadMapper tipoPropiedadMapper;

    public TipoPropiedadService(TipoPropiedadRepository tipoPropiedadRepository, TipoPropiedadMapper tipoPropiedadMapper) {
        super(tipoPropiedadRepository, tipoPropiedadMapper);
    }

    @Override
    @Transactional
    public TipoPropiedadResponseDTO update(Long id, TipoPropiedadDTO tipoPropiedadDTO) {
        // Validar que el tipo de propiedad existe
        TipoPropiedad tipoPropiedad = tipoPropiedadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de propiedad con el id " + id + " no encontrado"));

        if (!tipoPropiedadDTO.getDenominacion().equals(tipoPropiedad.getDenominacion())) {
            tipoPropiedad.setDenominacion(tipoPropiedadDTO.getDenominacion());
            tipoPropiedadRepository.save(tipoPropiedad);
        }
        return tipoPropiedadMapper.toResponseDTO(tipoPropiedadRepository.save(tipoPropiedad));
    }

    public List<TipoPropiedadResponseDTO> getActivos() {
        List<TipoPropiedad> tipoPropiedades = tipoPropiedadRepository.findByActivo(true);
        return tipoPropiedadMapper.toResponseDTOList(tipoPropiedades);
    }

    // Aquí puedes agregar métodos específicos para el servicio de TipoPropiedad si es necesario
}
