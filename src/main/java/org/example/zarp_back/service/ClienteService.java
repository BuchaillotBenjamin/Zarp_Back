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

import java.util.List;

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

    @Override
    @Transactional
    public ClienteResponseDTO update (Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));



        if (!clienteDTO.getUsuario().getNombreCompleto().equals(cliente.getUsuario().getNombreCompleto())){
            cliente.getUsuario().setNombreCompleto(clienteDTO.getUsuario().getNombreCompleto());
            clienteRepository.save(cliente);
        }

        return clienteMapper.toResponseDTO(cliente);
    }


    @Transactional
    public ClienteResponseDTO verificacionCorreo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setCorreoVerificado(true);
        clienteRepository.save(cliente);

        verificacionCompleta(id);

        return clienteMapper.toResponseDTO(cliente);

    }

    @Transactional
    public ClienteResponseDTO verificacionDocumentacion(Long id, boolean verificado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));

        List<VerificacionCliente> verificacionesActivas = verificacionClienteRepository.findVerificacionesActivasByClienteId(id);

        VerificacionCliente verificacionCliente = verificacionesActivas.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No hay verificación activa para el cliente con id: " + id));

        if (verificado) {
            cliente.setDocumentoVerificado(true);
            clienteRepository.save(cliente);
        }

        verificacionCliente.setActivo(false);
        verificacionClienteRepository.save(verificacionCliente);

        verificacionCompleta(id);

        return clienteMapper.toResponseDTO(cliente);

    }

    private void verificacionCompleta(long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + clienteId));

        if (cliente.getCorreoVerificado() && cliente.getDocumentoVerificado()) {
            cliente.setRol(Rol.PROPIETARIO);
            clienteRepository.save(cliente);
        }

    }

    // Aquí puedes agregar métodos específicos para el servicio de Cliente si es necesario
}
