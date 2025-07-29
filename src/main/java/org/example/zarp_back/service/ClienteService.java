package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService extends GenericoServiceImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long> {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        super(clienteRepository, clienteMapper);
    }

    @Override
    @Transactional
    public ClienteResponseDTO save(ClienteDTO clienteDTO) {

        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        //verificaciones
        cliente.setCorreoVerificado(false);
        cliente.setDocumentoVerificado(false);

        clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(cliente);
    }



    // Aquí puedes agregar métodos específicos para el servicio de Cliente si es necesario
}
