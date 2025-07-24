package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.VerificacionClienteMapper;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.repository.VerificacionClienteRepository;

public class VerificacionClienteService extends GenericoServiceImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long> {

    public VerificacionClienteService(VerificacionClienteRepository verificacionClienteRepository, VerificacionClienteMapper verificacionClienteMapper) {
        super(verificacionClienteRepository, verificacionClienteMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Verificación de Cliente si es necesario
}
