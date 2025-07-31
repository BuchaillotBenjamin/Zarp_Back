package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.config.mappers.PropiedadMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, PropiedadMapper.class})
public interface ReservaMapper extends GenericoMapper<Reserva, ReservaDTO, ReservaResponseDTO> {

    @Override
        // El clienteId y propiedadId en DTO deben ser convertidos manualmente en el service a entidades Cliente y Propiedad
    Reserva toEntity(ReservaDTO dto);

    @Override
    List<Reserva> toEntityList(List<ReservaDTO> dtos);

    @Override
    ReservaResponseDTO toResponseDTO(Reserva entity);

    @Override
    List<ReservaResponseDTO> toResponseDTOList(List<Reserva> entityList);
}
