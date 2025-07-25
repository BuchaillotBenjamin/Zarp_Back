package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ConversacionMapper;
import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.repository.ConversacionRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversacionService extends GenericoServiceImpl<Conversacion, ConversacionDTO, ConversacionResponseDTO, Long> {

    public ConversacionService(ConversacionRepository conversacionRepository, ConversacionMapper conversacionMapper) {
        super(conversacionRepository, conversacionMapper);
    }

}
