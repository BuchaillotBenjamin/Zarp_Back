package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.model.interfaces.GenericoMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends GenericoMapper<Empleado, EmpleadoDTO, EmpleadoResponseDTO> {

    @Override
    Empleado toEntity(EmpleadoDTO dto);

    @Override
    List<Empleado> toEntityList(List<EmpleadoDTO> dtos);

    @Override
    EmpleadoResponseDTO toResponseDTO(Empleado entity);

    @Override
    List<EmpleadoResponseDTO> toResponseDTOList(List<Empleado> entityList);
}
