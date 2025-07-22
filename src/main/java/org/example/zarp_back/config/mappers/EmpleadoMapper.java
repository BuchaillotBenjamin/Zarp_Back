package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.UsuarioMapper;
import org.example.zarp_back.config.mappers.DireccionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, DireccionMapper.class})
public interface EmpleadoMapper extends GenericoMapper<Empleado, EmpleadoDTO, EmpleadoResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Empleado toEntity(EmpleadoDTO dto);

    @Override
    List<Empleado> toEntityList(List<EmpleadoDTO> dtos);

    @Override
    EmpleadoResponseDTO toResponseDTO(Empleado entity);

    @Override
    List<EmpleadoResponseDTO> toResponseDTOList(List<Empleado> entityList);
}
