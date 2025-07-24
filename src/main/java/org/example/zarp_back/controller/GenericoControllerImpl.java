package org.example.zarp_back.controller;

import org.example.zarp_back.model.entity.Base;
import org.example.zarp_back.model.interfaces.GenericoController;
import org.example.zarp_back.service.GenericoServiceImpl;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public abstract class GenericoControllerImpl<E extends Base, D, R, ID extends Serializable,
        S extends GenericoServiceImpl<E, D, R, ID>> implements GenericoController<E, D, R, ID> {

    protected S s;

    public GenericoControllerImpl(S servicio) {
        this.s = servicio;
    }

    @Override
    public ResponseEntity<R> save(D dto) {
        R response = s.save(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<R> update(ID id, D dto) {
        R response = s.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> delete(ID id) {
        s.delete(id);
        return ResponseEntity.ok("Entidad eliminada correctamente");
    }

    @Override
    public ResponseEntity<R> findById(ID id) {
        R response = s.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<R>> findAll() {
        return ResponseEntity.ok(s.findAll());
    }






}
