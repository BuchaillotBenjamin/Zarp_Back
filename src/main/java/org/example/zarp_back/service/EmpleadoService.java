package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.EmpleadoMapper;
import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoService extends GenericoServiceImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long> {

    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoMapper empleadoMapper;

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        super(empleadoRepository, empleadoMapper);
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO update (Long id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado con el id " + id + " no encontrado"));

        boolean updated = false;

        if (!empleadoDTO.getUsuario().getCorreoElectronico().equals(empleado.getUsuario().getCorreoElectronico())){
            empleado.getUsuario().setCorreoElectronico(empleadoDTO.getUsuario().getCorreoElectronico());
            updated = true;
        }
        if (!empleadoDTO.getUsuario().getNombreCompleto().equals(empleado.getUsuario().getNombreCompleto())){
            empleado.getUsuario().setNombreCompleto(empleadoDTO.getUsuario().getNombreCompleto());
            updated = true;
        }
        if (!empleadoDTO.getTelefono().equals(empleado.getTelefono())){
            empleado.setTelefono(empleadoDTO.getTelefono());
            updated = true;
        }
        if (updated){
            empleado = empleadoRepository.save(empleado);
        }

        return empleadoMapper.toResponseDTO(empleado);
    }


    // Aquí puedes agregar métodos específicos para el servicio de Empleado si es necesario
}
