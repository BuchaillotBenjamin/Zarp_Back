package org.example.zarp_back.model.interfaces;

import java.util.List;

public interface GenericoService <T, D, R, ID> {

    R save(D dto);

    R update(ID id, D dto);

    R delete(ID id);

    R findById(ID id);

    List<R> findAll();

    R toggleActivo(ID id);

}
