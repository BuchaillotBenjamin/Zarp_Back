package org.example.zarp_back.model.interfaces;

import org.example.zarp_back.model.entity.Base;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface GenericoController <E extends Base, D, R, ID extends Serializable> {

    ResponseEntity<R> save(D dto);

    ResponseEntity<R> update(ID id, D dto);

    ResponseEntity<?> delete(ID id);

    ResponseEntity<R> findById(ID id);

    ResponseEntity<List<R>> findAll();

}
