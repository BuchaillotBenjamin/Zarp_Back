package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.service.TipoPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipoPropiedades")
public class TipoPropiedadController extends GenericoControllerImpl<TipoPropiedad, TipoPropiedadDTO, TipoPropiedadResponseDTO, Long, TipoPropiedadService> {

    @Autowired
    private TipoPropiedadService servicio;

    @Override
    protected String entidadNombre() {
        return "tipoPropiedades";
    }

    public TipoPropiedadController(TipoPropiedadService servicio) {
        super(servicio);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoPropiedadResponseDTO>> getActivos(Long id, TipoPropiedadDTO tipoPropiedadDTO) {
        List<TipoPropiedadResponseDTO> response = servicio.getActivos();
        return ResponseEntity.ok(response);
    }

    // Aquí puedes agregar métodos específicos para el controlador de TipoPropiedad si es necesario
}
