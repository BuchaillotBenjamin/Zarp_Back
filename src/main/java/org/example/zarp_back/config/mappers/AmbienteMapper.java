package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.ambiente.AmbienteDTO;
import org.example.zarp_back.model.dto.ambiente.AmbienteResponseDTO;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AmbienteMapper extends GenericoMapper<Ambiente, AmbienteDTO, AmbienteResponseDTO> {


    @Override
    Ambiente toEntity(AmbienteDTO dto);

    @Override
    List<Ambiente> toEntityList(List<AmbienteDTO> dtos);

    @Override
    AmbienteResponseDTO toResponseDTO(Ambiente ambiente) ;

    @Override
    List<AmbienteResponseDTO> toResponseDTOList(List<Ambiente> ambientes);
}
