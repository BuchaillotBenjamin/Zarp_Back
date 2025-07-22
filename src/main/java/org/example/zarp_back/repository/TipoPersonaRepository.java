package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.TipoPersona;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPersonaRepository extends GenericoRepository<TipoPersona, Long> {

}
