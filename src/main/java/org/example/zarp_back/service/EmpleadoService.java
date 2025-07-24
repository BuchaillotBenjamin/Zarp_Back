package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.EmpleadoMapper;
import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.repository.EmpleadoRepository;

public class EmpleadoService extends GenericoServiceImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long> {

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        super(empleadoRepository, empleadoMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Empleado si es necesario
}
