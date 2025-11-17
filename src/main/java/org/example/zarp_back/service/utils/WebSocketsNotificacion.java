package org.example.zarp_back.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketsNotificacion {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public <E> void NotificarSave(String entidadNombre, E objeto){

        messagingTemplate.convertAndSend("/topic/" + entidadNombre + "/save", objeto);

    }
    public <E> void NotificarUpdate(String entidadNombre, E objeto){

        messagingTemplate.convertAndSend("/topic/" + entidadNombre +"/update", objeto);

    }
    public <E> void NotificarDelete(String entidadNombre, E objeto){

        messagingTemplate.convertAndSend("/topic/" + entidadNombre+"/delete", objeto);

    }



}
