package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropiedadRepository extends GenericoRepository<Propiedad, Long> {

}
