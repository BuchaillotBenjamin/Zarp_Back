package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.dto.mensaje.MensajeResponseDTO;
import org.example.zarp_back.model.entity.Mensaje;
import org.example.zarp_back.service.MensajeService;

public class MensajeController extends GenericoControllerImpl<Mensaje, MensajeDTO, MensajeResponseDTO, Long, MensajeService> {

    public MensajeController(MensajeService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Mensaje si es necesario
}
