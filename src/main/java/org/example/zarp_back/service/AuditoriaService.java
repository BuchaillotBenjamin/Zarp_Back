package org.example.zarp_back.service;

import org.example.zarp_back.model.entity.Auditoria;
import org.example.zarp_back.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;


    public void registrar(String uid, String entidad, String accion, String entidadId) {
        Auditoria auditoria = Auditoria.builder()
                .uid(uid)
                .entidad(entidad)
                .accion(accion)
                .entidadId(entidadId)
                .timestamp(LocalDateTime.now())
                .build();

        auditoriaRepository.save(auditoria);
    }





}
