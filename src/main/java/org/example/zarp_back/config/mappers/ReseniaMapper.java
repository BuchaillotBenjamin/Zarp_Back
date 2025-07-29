package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.resenia.ReseniaDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaResponseDTO;
import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class})
public interface ReseniaMapper extends GenericoMapper<Resenia, ReseniaDTO, ReseniaResponseDTO> {

    @Override
    @Mapping(target = "usuarioId", ignore = true)
    @Mapping(target = "propiedadId", ignore = true)
    Resenia toEntity(ReseniaDTO dto);

    @Override
    List<Resenia> toEntityList(List<ReseniaDTO> dtos);

    @Override
    ReseniaResponseDTO toResponseDTO(Resenia entity);

    @Override
    List<ReseniaResponseDTO> toResponseDTOList(List<Resenia> entityList);
}
