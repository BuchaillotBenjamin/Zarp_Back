package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.UsuarioMapper;
import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.dto.usuario.UsuarioResponseDTO;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.repository.UsuarioRepository;

public class UsuarioService extends GenericoServiceImpl<Usuario, UsuarioDTO, UsuarioResponseDTO, Long> {

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        super(usuarioRepository, usuarioMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Usuario si es necesario
}
