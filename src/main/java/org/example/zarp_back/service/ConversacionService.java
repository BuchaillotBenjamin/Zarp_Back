package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ConversacionMapper;
import org.example.zarp_back.model.dto.conversacion.ConversacionDTO;
import org.example.zarp_back.model.dto.conversacion.ConversacionResponseDTO;
import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.model.entity.Mensaje;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.ConversacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversacionService extends GenericoServiceImpl<Conversacion, ConversacionDTO, ConversacionResponseDTO, Long> {

    @Autowired
    private ConversacionRepository conversacionRepository;
    @Autowired
    private ConversacionMapper conversacionMapper;
    @Autowired
    ClienteRepository clienteRepository;



    public ConversacionService(ConversacionRepository conversacionRepository, ConversacionMapper conversacionMapper) {
        super(conversacionRepository, conversacionMapper);
    }

    @Override
    @Transactional
    public ConversacionResponseDTO save(ConversacionDTO conversacionDTO) {

        if (conversacionDTO.getCliente1Id().equals(conversacionDTO.getCliente2Id())) {
            throw new IllegalArgumentException("Los clientes no pueden ser los mismos");
        }else if (conversacionRepository.existsByClienteIds(conversacionDTO.getCliente1Id(), conversacionDTO.getCliente2Id())) {
            throw new IllegalArgumentException("Ya existe una conversación entre estos clientes.");
        }

        //cliente1 y cliente2
        Cliente cliente1 = clienteRepository.findById(conversacionDTO.getCliente1Id())
                .orElseThrow(() -> new NotFoundException("Cliente 1 no encontrado"));
        Cliente cliente2 = clienteRepository.findById(conversacionDTO.getCliente2Id())
                .orElseThrow(() -> new NotFoundException("Cliente 2 no encontrado"));

        //mensajes
        Conversacion conversacion = Conversacion.builder()
                .fechaCreacion(LocalDate.now())
                .cliente1(cliente1)
                .cliente2(cliente2)
                .mensajes(new ArrayList<>())
                .build();

        //agregar mensajes

        for(MensajeDTO mensajeDTO : conversacionDTO.getMensajes()){
            cargarMensajes(mensajeDTO, conversacion);
        }

        conversacionRepository.save(conversacion);

        return conversacionMapper.toResponseDTO(conversacion);
    }

    public ConversacionResponseDTO agregarMensajes(Long conversacionId, MensajeDTO mensajeDTO) {

        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new NotFoundException("Conversación no encontrada con ID: " + conversacionId));

        //agregar mensaje
        cargarMensajes(mensajeDTO, conversacion);

        conversacionRepository.save(conversacion);

        return conversacionMapper.toResponseDTO(conversacion);
    }

    private void cargarMensajes(MensajeDTO mensajeDTO, Conversacion conversacion) {

        Cliente cliente = clienteRepository.findById(mensajeDTO.getEmisorId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + mensajeDTO.getEmisorId()));

        Mensaje mensaje = Mensaje.builder()
                .contenido(mensajeDTO.getContenido())
                .fechaEnvio(LocalDate.now())
                .horaEnvio(LocalTime.now())
                .conversacion(conversacion)
                .emisor(cliente)
                .build();

        conversacion.getMensajes().add(mensaje);

    }

}
