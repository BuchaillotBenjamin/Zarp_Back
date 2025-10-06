package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.dto.ambiente.AmbienteDTO;
import org.example.zarp_back.model.dto.ambiente.AmbienteResponseDTO;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.service.AmbienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambientes")
@Slf4j
public class AmbienteController extends GenericoControllerImpl<Ambiente, AmbienteDTO, AmbienteResponseDTO, Long, AmbienteService> {


    @Autowired
    private AmbienteService ambienteServicio;

    @Override
    protected String entidadNombre() {
        return "ambientes";
    }

    public AmbienteController(AmbienteService servicio) {
        super(servicio);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<AmbienteResponseDTO>> getActivos(HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");

        log.info("UID del usuario autenticado: " + uid + " accedió a /api/ambientes/activos");

        List<AmbienteResponseDTO> ambientesActivos = ambienteServicio.getActivos();

        return ResponseEntity.ok(ambientesActivos);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Ambiente si es necesario
}
