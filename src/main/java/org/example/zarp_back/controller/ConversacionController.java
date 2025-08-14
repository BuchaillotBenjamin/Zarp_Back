package org.example.zarp_back.controller;

import jakarta.validation.Valid;
import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.service.ConversacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversaciones")
public class ConversacionController extends GenericoControllerImpl<Conversacion, ConversacionDTO, ConversacionResponseDTO, Long, ConversacionService> {

    @Autowired
    ConversacionService conversacionService;

    @Override
    protected String entidadNombre() {
        return "conversaciones";
    }

    public ConversacionController(ConversacionService servicio) {
        super(servicio);
    }

    @PutMapping("/agregar-mensaje/{idConversacion}")
    public ResponseEntity<ConversacionResponseDTO> agregarMensaje(@Valid @RequestBody MensajeDTO mensajeDTO, @PathVariable Long idConversacion) {

        return ResponseEntity.ok(conversacionService.agregarMensajes(idConversacion, mensajeDTO));

    }


    // Aquí puedes agregar métodos específicos para el controlador de Conversación si es necesario
}
