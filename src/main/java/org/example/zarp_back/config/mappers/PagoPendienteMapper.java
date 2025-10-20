package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.pagosPendientes.PagoPendienteResponseDTO;
import org.example.zarp_back.model.entity.PagoPendiente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ClienteMapper.class)
public interface PagoPendienteMapper {

    PagoPendienteResponseDTO toDto(PagoPendiente entity);

    List<PagoPendienteResponseDTO> toDtoList(List<PagoPendiente> entities);
}