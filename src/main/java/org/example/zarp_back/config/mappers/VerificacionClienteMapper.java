package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.ImagenMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class, ClienteMapper.class})
public interface VerificacionClienteMapper extends GenericoMapper<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO> {

    @Override
    @Mapping(target = "clienteId", ignore = true)
    VerificacionCliente toEntity(VerificacionClienteDTO dto);

    @Override
    List<VerificacionCliente> toEntityList(List<VerificacionClienteDTO> dtos);

    @Override
    VerificacionClienteResponseDTO toResponseDTO(VerificacionCliente entity);

    @Override
    List<VerificacionClienteResponseDTO> toResponseDTOList(List<VerificacionCliente> entityList);
}
