package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.VerificacionClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService extends GenericoServiceImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long> {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private VerificacionClienteRepository verificacionClienteRepository;

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
        // Asignar rol por defecto
        cliente.setRol(Rol.CLIENTE);

        clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO verificacionCorreo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setCorreoVerificado(true);
        clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(cliente);

    }

    @Transactional
    public ClienteResponseDTO verificacionDocumentacion(Long id, boolean verificado, Long verificacionClienteId) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));

        VerificacionCliente verificacionCliente = verificacionClienteRepository.findById(verificacionClienteId)
                .orElseThrow(() -> new NotFoundException("Verificación de cliente no encontrada con id: " + verificacionClienteId));

        if (verificado) {
            cliente.setDocumentoVerificado(true);
            clienteRepository.save(cliente);
        }

        verificacionCliente.setActivo(false);
        verificacionClienteRepository.save(verificacionCliente);

        return clienteMapper.toResponseDTO(cliente);

    }



    // Aquí puedes agregar métodos específicos para el servicio de Cliente si es necesario
}
