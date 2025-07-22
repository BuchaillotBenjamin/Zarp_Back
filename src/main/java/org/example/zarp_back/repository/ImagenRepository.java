package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends GenericoRepository<Imagen, Long> {

}
