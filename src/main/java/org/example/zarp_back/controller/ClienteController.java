package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.service.ClienteService;

public class ClienteController extends GenericoControllerImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long, ClienteService> {

    public ClienteController(ClienteService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Cliente si es necesario
}
