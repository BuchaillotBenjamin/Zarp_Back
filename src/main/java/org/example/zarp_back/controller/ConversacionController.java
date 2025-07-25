package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.service.ConversacionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversaciones")
public class ConversacionController extends GenericoControllerImpl<Conversacion, ConversacionDTO, ConversacionResponseDTO, Long, ConversacionService> {

    public ConversacionController(ConversacionService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Conversación si es necesario
}
