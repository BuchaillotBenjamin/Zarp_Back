package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.DetalleCaracteristica;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.CaracteristicaMapper; // Para mapear Caracteristica
// Podrías importar PropiedadMapper si lo necesitás en el futuro

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CaracteristicaMapper.class})
public interface DetalleCaracteristicaMapper extends GenericoMapper<DetalleCaracteristica, DetalleCaracteristicaDTO, DetalleCaracteristicaResponseDTO> {

    @Override
    DetalleCaracteristica toEntity(DetalleCaracteristicaDTO dto);


    @Override
    List<DetalleCaracteristica> toEntityList(List<DetalleCaracteristicaDTO> dtos);

    @Override
    DetalleCaracteristicaResponseDTO toResponseDTO(DetalleCaracteristica entity);

    @Override
    List<DetalleCaracteristicaResponseDTO> toResponseDTOList(List<DetalleCaracteristica> entityList);
}
