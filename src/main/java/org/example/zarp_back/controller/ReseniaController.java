package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.reseña.ReseniaDTO;
import org.example.zarp_back.model.dto.reseña.ReseniaResponseDTO;
import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.service.ReseniaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resenias")
public class ReseniaController extends GenericoControllerImpl<Resenia, ReseniaDTO, ReseniaResponseDTO, Long, ReseniaService> {

    public ReseniaController(ReseniaService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Resenia si es necesario
}
