package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.repository.ClienteRepository;

public class ClienteService extends GenericoServiceImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long> {

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        super(clienteRepository, clienteMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Cliente si es necesario
}
