package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Mensaje;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends GenericoRepository<Mensaje, Long> {

}
