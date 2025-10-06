package org.example.zarp_back.model.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.example.zarp_back.model.entity.Base;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface GenericoController <E extends Base, D, R, ID extends Serializable> {

    ResponseEntity<R> save(D dto, HttpServletRequest request);

    ResponseEntity<R> update(ID id, D dto, HttpServletRequest request);

    ResponseEntity<R> delete(ID id, HttpServletRequest request);

    ResponseEntity<R> findById(ID id);

    ResponseEntity<List<R>> findAll();

    ResponseEntity<R> toggleActivo(ID id, HttpServletRequest request);

}
