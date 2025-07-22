package org.example.zarp_back.config.mappers;

import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.dto.usuario.UsuarioResponseDTO;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends GenericoMapper<Usuario, UsuarioDTO, UsuarioResponseDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    @Override
    List<Usuario> toEntityList(List<UsuarioDTO> dtos);

    @Override
        // Aquí no incluimos la contraseña en el response, así que no mapeamos ese campo
    UsuarioResponseDTO toResponseDTO(Usuario entity);

    @Override
    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> entityList);
}
