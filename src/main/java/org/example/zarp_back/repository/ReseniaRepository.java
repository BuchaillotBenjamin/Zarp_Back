package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReseniaRepository extends GenericoRepository<Resenia, Long> {


    boolean existsByPropiedadIdAndUsuarioId(Long propiedadId, Long usuarioId);

    Optional<Resenia> findByPropiedadIdAndUsuarioId(Long propiedadId, Long usuarioId);



}
