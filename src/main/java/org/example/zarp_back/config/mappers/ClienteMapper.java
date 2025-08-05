package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper extends GenericoMapper<Cliente, ClienteDTO, ClienteResponseDTO> {

    @Override
    Cliente toEntity(ClienteDTO dto);

    @Override
    List<Cliente> toEntityList(List<ClienteDTO> dtos);

    @Override
    ClienteResponseDTO toResponseDTO(Cliente entity);

    @Override
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> entityList);
}
