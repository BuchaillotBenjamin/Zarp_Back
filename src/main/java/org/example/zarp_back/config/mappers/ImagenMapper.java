package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.dto.imagen.ImagenResponseDTO;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagenMapper extends GenericoMapper<Imagen, ImagenDTO, ImagenResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    Imagen toEntity(ImagenDTO dto);

    @Override
    List<Imagen> toEntityList(List<ImagenDTO> dtos);

    @Override
    ImagenResponseDTO toResponseDTO(Imagen entity);

    @Override
    List<ImagenResponseDTO> toResponseDTOList(List<Imagen> entityList);
}
