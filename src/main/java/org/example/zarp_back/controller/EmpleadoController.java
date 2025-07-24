package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.service.EmpleadoService;

public class EmpleadoController extends GenericoControllerImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long, EmpleadoService> {

    public EmpleadoController(EmpleadoService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Empleado si es necesario
}
