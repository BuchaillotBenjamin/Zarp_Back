package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaDTO;
import org.example.zarp_back.model.dto.caracteristicas.CaracteristicaResponseDTO;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/caracteristicas")
@Slf4j
public class CaracteristicaController extends GenericoControllerImpl<Caracteristica, CaracteristicaDTO, CaracteristicaResponseDTO, Long, CaracteristicaService> {

    @Autowired
    private CaracteristicaService servicio;

    @Override
    protected String entidadNombre() {
        return "caracteristicas";
    }

    public CaracteristicaController(CaracteristicaService servicio) {
        super(servicio);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CaracteristicaResponseDTO>> getActivos( HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID del usuario autenticado: " + uid);

        List<CaracteristicaResponseDTO> caracteristicas = servicio.getActivos();

        return ResponseEntity.ok(caracteristicas);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Característica si es necesario
}
