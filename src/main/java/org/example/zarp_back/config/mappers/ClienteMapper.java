package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.UsuarioMapper;  // Importá el mapper de Usuario
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface ClienteMapper extends GenericoMapper<Cliente, ClienteDTO, ClienteResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Cliente toEntity(ClienteDTO dto);

    @Override
    List<Cliente> toEntityList(List<ClienteDTO> dtos);

    @Override
    ClienteResponseDTO toResponseDTO(Cliente entity);

    @Override
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> entityList);
}
