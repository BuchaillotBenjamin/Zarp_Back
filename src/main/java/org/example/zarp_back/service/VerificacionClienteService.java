package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.VerificacionClienteMapper;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.VerificacionClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificacionClienteService extends GenericoServiceImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long> {

    @Autowired
    private VerificacionClienteRepository verificacionClienteRepository;
    @Autowired
    private VerificacionClienteMapper verificacionClienteMapper;
    @Autowired
    private ClienteRepository clienteRepository;


    public VerificacionClienteService(VerificacionClienteRepository verificacionClienteRepository, VerificacionClienteMapper verificacionClienteMapper) {
        super(verificacionClienteRepository, verificacionClienteMapper);
    }

    @Override
    @Transactional
    public VerificacionClienteResponseDTO save(VerificacionClienteDTO verificacionClienteDTO) {

        Cliente cliente = clienteRepository.findById(verificacionClienteDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + verificacionClienteDTO.getClienteId()));

        if(verificacionClienteRepository.findVerificacionesActivasByClienteId(verificacionClienteDTO.getClienteId()).size() > 0) {
            throw new RuntimeException("El cliente ya tiene una verificación activa.");
        }else if(cliente.getDocumentoVerificado()){
            throw new RuntimeException("El cliente ya esta verificado.");
        }

        VerificacionCliente verificacionCliente = verificacionClienteMapper.toEntity(verificacionClienteDTO);

        //cliente
        verificacionCliente.setCliente(cliente);

        verificacionClienteRepository.save(verificacionCliente);

        return verificacionClienteMapper.toResponseDTO(verificacionCliente);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Verificación de Cliente si es necesario
}
