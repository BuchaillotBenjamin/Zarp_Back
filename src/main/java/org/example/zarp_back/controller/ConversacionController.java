package org.example.zarp_back.controller;

import jakarta.validation.Valid;
import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.service.ConversacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversaciones")
public class ConversacionController extends GenericoControllerImpl<Conversacion, ConversacionDTO, ConversacionResponseDTO, Long, ConversacionService> {

    @Autowired
    private ConversacionService conversacionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    protected String entidadNombre() {
        return "conversaciones";
    }

    public ConversacionController(ConversacionService servicio) {
        super(servicio);
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<ConversacionResponseDTO> save(@Valid @RequestBody ConversacionDTO dto) {
        ConversacionResponseDTO response = conversacionService.save(dto);
        messagingTemplate.convertAndSend("/topic/conversaciones/save/"+response.getCliente2().getId(), response);
        messagingTemplate.convertAndSend("/topic/conversaciones/save/"+response.getCliente1().getId(), response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/agregar-mensaje/{idConversacion}")
    public ResponseEntity<ConversacionResponseDTO> agregarMensaje(@Valid @RequestBody MensajeDTO mensajeDTO, @PathVariable Long idConversacion) {

        ConversacionResponseDTO response = conversacionService.agregarMensajes(idConversacion, mensajeDTO);
        messagingTemplate.convertAndSend("/topic/conversaciones/update/"+response.getCliente2().getId(), response);
        messagingTemplate.convertAndSend("/topic/conversaciones/update/"+response.getCliente1().getId(), response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerConversacionesPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(conversacionService.findByClienteId(clienteId));
    }


    // Aquí puedes agregar métodos específicos para el controlador de Conversación si es necesario
}
