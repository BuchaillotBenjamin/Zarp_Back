package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.TipoPropiedad;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPropiedadRepository extends GenericoRepository<TipoPropiedad, Long> {

}
