package org.example.zarp_back.controller;

import org.example.zarp_back.model.entity.Base;
import org.example.zarp_back.model.interfaces.GenericoController;
import org.example.zarp_back.model.interfaces.GenericoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class GenericoControllerImpl<E extends Base, D, R, ID extends Serializable,
        S extends GenericoService<E, D, R, ID>> implements GenericoController<E, D, R, ID> {

    protected S s;

    public GenericoControllerImpl(S servicio) {
        this.s = servicio;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<R> save(@RequestBody D dto) {
        R response = s.save(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<R> update(@PathVariable ID id, @RequestBody D dto) {
        R response = s.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        s.delete(id);
        return ResponseEntity.ok("Entidad eliminada correctamente");
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<R> findById(@PathVariable ID id) {
        R response = s.findById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<R>> findAll() {
        return ResponseEntity.ok(s.findAll());
    }






}
