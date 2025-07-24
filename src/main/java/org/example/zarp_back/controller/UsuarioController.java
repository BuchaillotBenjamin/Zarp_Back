package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.dto.usuario.UsuarioResponseDTO;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.service.UsuarioService;

public class UsuarioController extends GenericoControllerImpl<Usuario, UsuarioDTO, UsuarioResponseDTO, Long, UsuarioService> {

    public UsuarioController(UsuarioService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Usuario si es necesario
}
