package org.example.zarp_back.model.interfaces;


import org.example.zarp_back.model.entity.Base;

import java.util.List;

public interface GenericoMapper<E extends Base, D, R> {

    E toEntity(D dto);
    R toResponseDTO(E entity);

    List<E> toEntityList(List<D> dtoList);
    List<R> toResponseDTOList(List<E> entityList);
}