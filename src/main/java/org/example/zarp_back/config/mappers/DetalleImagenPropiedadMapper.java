package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadResponseDTO;
import org.example.zarp_back.model.entity.DetalleImagenPropiedad;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.config.mappers.ImagenMapper;  // Para mapear Imagen
// Podrías importar PropiedadMapper si lo necesitás para otro uso

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class})
public interface DetalleImagenPropiedadMapper extends GenericoMapper<DetalleImagenPropiedad, DetalleImagenPropiedadDTO, DetalleImagenPropiedadResponseDTO> {

    @Override
        // Recuerda que la propiedadId en el DTO debe mapearse manualmente a la entidad Propiedad en el service
    DetalleImagenPropiedad toEntity(DetalleImagenPropiedadDTO dto);

    @Override
    List<DetalleImagenPropiedad> toEntityList(List<DetalleImagenPropiedadDTO> dtos);

    @Override
    DetalleImagenPropiedadResponseDTO toResponseDTO(DetalleImagenPropiedad entity);

    @Override
    List<DetalleImagenPropiedadResponseDTO> toResponseDTOList(List<DetalleImagenPropiedad> entityList);
}
