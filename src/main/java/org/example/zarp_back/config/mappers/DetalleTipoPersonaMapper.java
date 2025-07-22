package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaResponseDTO;
import org.example.zarp_back.model.entity.DetalleTipoPersona;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.TipoPersonaMapper; // Para mapear TipoPersona

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TipoPersonaMapper.class})
public interface DetalleTipoPersonaMapper extends GenericoMapper<DetalleTipoPersona, DetalleTipoPersonaDTO, DetalleTipoPersonaResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    DetalleTipoPersona toEntity(DetalleTipoPersonaDTO dto);

    @Override
    List<DetalleTipoPersona> toEntityList(List<DetalleTipoPersonaDTO> dtos);

    @Override
    DetalleTipoPersonaResponseDTO toResponseDTO(DetalleTipoPersona entity);

    @Override
    List<DetalleTipoPersonaResponseDTO> toResponseDTOList(List<DetalleTipoPersona> entityList);
}
