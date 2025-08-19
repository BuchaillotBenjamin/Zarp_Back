package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.empleado.EmpleadoDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController extends GenericoControllerImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long, EmpleadoService> {

    @Autowired
    private EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService servicio) {
        super(servicio);
    }

    @GetMapping("/existe-uid/{uid}")
    public ResponseEntity<Boolean> existsByUid(String uid) {

        return ResponseEntity.ok(empleadoService.existsByUid(uid));

    }

    @GetMapping("/getByUid/{uid}")
    public ResponseEntity<EmpleadoResponseDTO> getByUid(String uid) {

        EmpleadoResponseDTO empleado = empleadoService.getByUid(uid);

        return ResponseEntity.ok(empleado);

    }

}
