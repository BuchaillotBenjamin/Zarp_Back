package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.DetalleCaracteristica;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCaracteristicaRepository extends GenericoRepository<DetalleCaracteristica, Long> {

}
