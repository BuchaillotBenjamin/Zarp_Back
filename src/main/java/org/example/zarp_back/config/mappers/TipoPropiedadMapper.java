package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoPropiedadMapper extends GenericoMapper<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO> {

    @Override
    TipoPropiedad toEntity(TipoPropiedadDTO dto);

    @Override
    List<TipoPropiedad> toEntityList(List<TipoPropiedadDTO> dtos);

    @Override
    TipoPropiedadResponseDTO toResponseDTO(TipoPropiedad entity);

    @Override
    List<TipoPropiedadResponseDTO> toResponseDTOList(List<TipoPropiedad> entityList);
}
