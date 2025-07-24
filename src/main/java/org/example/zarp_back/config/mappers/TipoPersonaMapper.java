package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaDTO;
import org.example.zarp_back.model.dto.tipoPersona.TipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoPersonaMapper extends GenericoMapper<TipoPersona, TipoPersonaDTO, TipoPersonaResponseDTO> {

    @Override
    TipoPersona toEntity(TipoPersonaDTO dto);

    @Override
    List<TipoPersona> toEntityList(List<TipoPersonaDTO> dtos);

    @Override
    TipoPersonaResponseDTO toResponseDTO(TipoPersona entity);

    @Override
    List<TipoPersonaResponseDTO> toResponseDTOList(List<TipoPersona> entityList);
}
