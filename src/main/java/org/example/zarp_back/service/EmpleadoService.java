package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.EmpleadoMapper;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class EmpleadoService extends GenericoServiceImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long> {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private EmpleadoMapper empleadoMapper;
    @Autowired
    private ClienteRepository clienteRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        super(empleadoRepository, empleadoMapper);
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO save(EmpleadoDTO empleadoDTO) {

        if (clienteRepository.existsByUid(empleadoDTO.getUid())|| empleadoRepository.existsByUid(empleadoDTO.getUid())) {
            throw new IllegalArgumentException("El UID ya está en uso");
        }

        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);

        empleadoRepository.save(empleado);

        return empleadoMapper.toResponseDTO(empleado);
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO update (Long id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id " + id + " no encontrado"));


        if (!empleadoDTO.getNombreCompleto().equals(empleado.getNombreCompleto())){
            empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
            empleado = empleadoRepository.save(empleado);
        }

        return empleadoMapper.toResponseDTO(empleado);
    }

    public Boolean existsByUid(String uid) {
        return empleadoRepository.existsByUid(uid);
    }

    public EmpleadoResponseDTO getByUid(String uid) {
        Empleado empleado = empleadoRepository.findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Empleado con el UID " + uid + " no encontrado"));

        return empleadoMapper.toResponseDTO(empleado);
    }


    public ClienteResponseDTO getByUidLogin(String uid){

        Empleado empleado = empleadoRepository.findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Empleado con el UID " + uid + " no encontrado"));

        ClienteResponseDTO empleadoResponse = ClienteResponseDTO.builder()
                .id(empleado.getId())
                .activo(empleado.getActivo())
                .uid(empleado.getUid())
                .rol(empleado.getRol())
                .correoElectronico(empleado.getCorreoElectronico())
                .nombreCompleto(empleado.getNombreCompleto())
                .fotoPerfil(null)
                .documentoVerificado(null)
                .correoVerificado(null)
                .build();

        return empleadoResponse;

    }

    // Aquí puedes agregar métodos específicos para el servicio de Empleado si es necesario
}
