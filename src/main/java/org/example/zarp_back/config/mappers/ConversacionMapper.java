package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.MensajeMapper;  // Importamos el mapper de Mensaje
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MensajeMapper.class})
public interface ConversacionMapper extends GenericoMapper<Conversacion, ConversacionDTO, ConversacionResponseDTO> {

    @Override
    Conversacion toEntity(ConversacionDTO dto);

    @Override
    List<Conversacion> toEntityList(List<ConversacionDTO> dtos);

    @Override
    ConversacionResponseDTO toResponseDTO(Conversacion entity);

    @Override
    List<ConversacionResponseDTO> toResponseDTOList(List<Conversacion> entityList);
}
