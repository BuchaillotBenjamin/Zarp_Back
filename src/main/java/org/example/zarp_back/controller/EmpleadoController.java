package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.service.EmpleadoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController extends GenericoControllerImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long, EmpleadoService> {

    @Override
    protected String entidadNombre() {
        return "empleados";
    }

    public EmpleadoController(EmpleadoService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Empleado si es necesario
}
