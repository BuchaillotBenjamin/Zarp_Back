package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.dto.mensaje.MensajeResponseDTO;
import org.example.zarp_back.model.entity.Mensaje;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.config.mappers.ConversacionMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ConversacionMapper.class})
public interface MensajeMapper extends GenericoMapper<Mensaje, MensajeDTO, MensajeResponseDTO> {

    @Override
    @Mapping(target = "emisorId", ignore = true)
    @Mapping(target = "receptorId", ignore = true)
    @Mapping(target = "conversacionId", ignore = true)
    Mensaje toEntity(MensajeDTO dto);

    @Override
    List<Mensaje> toEntityList(List<MensajeDTO> dtos);

    @Override
    MensajeResponseDTO toResponseDTO(Mensaje entity);

    @Override
    List<MensajeResponseDTO> toResponseDTOList(List<Mensaje> entityList);
}
