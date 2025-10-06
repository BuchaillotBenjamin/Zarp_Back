package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.entity.Base;
import org.example.zarp_back.model.interfaces.GenericoController;
import org.example.zarp_back.model.interfaces.GenericoService;
import org.example.zarp_back.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Slf4j
public abstract class GenericoControllerImpl<E extends Base, D, R, ID extends Serializable,
        S extends GenericoService<E, D, R, ID>> implements GenericoController<E, D, R, ID> {

    protected S s;
    @Autowired
    protected SimpMessagingTemplate messagingTemplate;
    protected AuditoriaService auditoriaService;
    protected abstract String entidadNombre();

    public GenericoControllerImpl(S servicio) {
        this.s = servicio;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<R> save(@Valid @RequestBody D dto, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID del usuario autenticado: " + uid);

        R response = s.save(dto);
        messagingTemplate.convertAndSend("/topic/" + entidadNombre() + "/save", response);
        auditoriaService.registrar(uid, entidadNombre(), "CREATE", response.toString());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<R> update(@PathVariable ID id, @Valid @RequestBody D dto, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID del usuario autenticado: " + uid);
        R response = s.update(id, dto);
        messagingTemplate.convertAndSend("/topic/" + entidadNombre() + "/update", response);
        auditoriaService.registrar(uid, entidadNombre(), "UPDATE", response.toString());
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<R> delete(@PathVariable ID id, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID del usuario autenticado: " + uid);
        R response = s.delete(id);
        messagingTemplate.convertAndSend("/topic/" + entidadNombre() + "/delete", response);
        auditoriaService.registrar(uid, entidadNombre(), "DELETE", response.toString());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/getById/{id}")
    public ResponseEntity<R> findById(@PathVariable ID id) {
        R response = s.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<R>> findAll() {
        return ResponseEntity.ok(s.findAll());
    }

    @Override
    @PatchMapping("/toggleActivo/{id}")
    public ResponseEntity<R> toggleActivo(@PathVariable ID id, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID del usuario autenticado: " + uid);

        R response = s.toggleActivo(id);
        messagingTemplate.convertAndSend("/topic/" + entidadNombre() + "/update", response);
        auditoriaService.registrar(uid, entidadNombre(), "TOGGLE_ACTIVO", response.toString());
        return ResponseEntity.ok(response);
    }






}
