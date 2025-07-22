package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbienteRepository  extends GenericoRepository<Ambiente, Long> {


}
