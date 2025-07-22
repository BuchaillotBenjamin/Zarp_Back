package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends GenericoRepository<Direccion, Long> {

}
