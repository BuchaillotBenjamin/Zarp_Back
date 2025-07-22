package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaRepository extends GenericoRepository<Caracteristica, Long> {

}
