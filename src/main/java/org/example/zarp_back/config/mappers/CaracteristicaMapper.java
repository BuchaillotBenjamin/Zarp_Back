package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class})
public interface CaracteristicaMapper extends GenericoMapper<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Caracteristica toEntity(CaracteristicaDTO dto);

    @Override
    List<Caracteristica> toEntityList(List<CaracteristicaDTO> dtos);

    @Override
    CaracteristicaResponseDTO toResponseDTO(Caracteristica entity);

    @Override
    List<CaracteristicaResponseDTO> toResponseDTOList(List<Caracteristica> entityList);
}
