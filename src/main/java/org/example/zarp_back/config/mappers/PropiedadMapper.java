package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                DireccionMapper.class,
                TipoPropiedadMapper.class,
                ReseniaMapper.class,
                DetalleTipoPersonaMapper.class,
                DetalleCaracteristicaMapper.class,
                DetalleImagenPropiedadMapper.class,
                DetalleAmbienteMapper.class
        })
public interface PropiedadMapper extends GenericoMapper<Propiedad, PropiedadDTO, PropiedadResponseDTO> {

    @Override
    @Mapping(target = "detalleTipoPersonas", ignore = true)
    @Mapping(target = "detalleCaracteristicas", ignore = true)
    @Mapping(target = "detalleImagenes", ignore = true)
    @Mapping(target = "detalleAmbientes", ignore = true)

    Propiedad toEntity(PropiedadDTO dto);

    @Override
    List<Propiedad> toEntityList(List<PropiedadDTO> dtos);

    @Override
    PropiedadResponseDTO toResponseDTO(Propiedad entity);

    @Override
    List<PropiedadResponseDTO> toResponseDTOList(List<Propiedad> entityList);
}
