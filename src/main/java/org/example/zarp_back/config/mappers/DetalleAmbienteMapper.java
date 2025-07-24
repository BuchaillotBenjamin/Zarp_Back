package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteResponseDTO;
import org.example.zarp_back.model.entity.DetalleAmbiente;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.AmbienteMapper; // Para mapear Ambiente
// Si luego quieres mapear Propiedad (por ejemplo para métodos que reciban Propiedad DTO), importá PropiedadMapper

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AmbienteMapper.class})
public interface DetalleAmbienteMapper extends GenericoMapper<DetalleAmbiente, DetalleAmbienteDTO, DetalleAmbienteResponseDTO> {

    @Override
        //TODO: Mapear campos de DetalleAmbienteDTO a DetalleAmbiente.
        // Como DetalleAmbienteDTO tiene propiedadId y ambienteId (Long),
        // y DetalleAmbiente tiene entidades Propiedad y Ambiente,
        // si necesitás hacer la conversión explícita de IDs a entidades deberías manejarlo fuera o con mapeos personalizados.
    DetalleAmbiente toEntity(DetalleAmbienteDTO dto);

    @Override
    List<DetalleAmbiente> toEntityList(List<DetalleAmbienteDTO> dtos);

    @Override
    DetalleAmbienteResponseDTO toResponseDTO(DetalleAmbiente entity);

    @Override
    List<DetalleAmbienteResponseDTO> toResponseDTOList(List<DetalleAmbiente> entityList);
}
