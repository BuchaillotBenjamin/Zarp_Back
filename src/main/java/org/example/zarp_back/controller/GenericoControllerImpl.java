package org.example.zarp_back.controller;

import jakarta.validation.Valid;
import org.example.zarp_back.model.entity.Base;
import org.example.zarp_back.model.interfaces.GenericoController;
import org.example.zarp_back.model.interfaces.GenericoService;
import org.example.zarp_back.service.utils.WebSocketsNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class GenericoControllerImpl<E extends Base, D, R, ID extends Serializable,
        S extends GenericoService<E, D, R, ID>> implements GenericoController<E, D, R, ID> {

    protected S s;
    @Autowired
    protected WebSocketsNotificacion webSocketsNotificacion;
    protected abstract String entidadNombre();

    public GenericoControllerImpl(S servicio) {
        this.s = servicio;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<R> save(@Valid @RequestBody D dto) {
        R response = s.save(dto);
        webSocketsNotificacion.NotificarSave(entidadNombre(), response);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<R> update(@PathVariable ID id,@Valid @RequestBody D dto) {
        R response = s.update(id, dto);
        webSocketsNotificacion.NotificarUpdate(entidadNombre(), response);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<R> delete(@PathVariable ID id) {
        R response = s.delete(id);
        webSocketsNotificacion.NotificarDelete(entidadNombre(), response);
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
    public ResponseEntity<R> toggleActivo(@PathVariable ID id) {
        R response= s.toggleActivo(id);
        webSocketsNotificacion.NotificarUpdate(entidadNombre(), response);
        return ResponseEntity.ok(response);
    }





}
