package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/caracteristicas")
public class CaracteristicaController extends GenericoControllerImpl<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO, Long, CaracteristicaService> {

    @Autowired
    private CaracteristicaService servicio;

    public CaracteristicaController(CaracteristicaService servicio) {
        super(servicio);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CaracteristicaResponseDTO>> getActivos(Long propiedadId) {
        List<CaracteristicaResponseDTO> caracteristicas = servicio.getActivos(propiedadId);
        return ResponseEntity.ok(caracteristicas);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Característica si es necesario
}
