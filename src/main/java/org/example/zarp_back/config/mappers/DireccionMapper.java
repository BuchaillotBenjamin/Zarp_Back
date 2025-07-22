package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.direccion.DireccionDTO;
import org.example.zarp_back.model.dto.direccion.DireccionResponseDTO;
import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DireccionMapper extends GenericoMapper<Direccion, DireccionDTO, DireccionResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Direccion toEntity(DireccionDTO dto);

    @Override
    List<Direccion> toEntityList(List<DireccionDTO> dtos);

    @Override
    DireccionResponseDTO toResponseDTO(Direccion entity);

    @Override
    List<DireccionResponseDTO> toResponseDTOList(List<Direccion> entityList);
}
