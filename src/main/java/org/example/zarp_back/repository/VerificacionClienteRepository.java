package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificacionClienteRepository extends GenericoRepository<VerificacionCliente, Long> {

    @Query("SELECT vc " +
            "FROM VerificacionCliente vc " +
            "WHERE vc.cliente.id = :clienteId " +
            "AND vc.activo = true")
    Optional<VerificacionCliente> findVerificacionActivaByClienteId(@Param("clienteId") Long clienteId);


    @Query("SELECT CASE WHEN COUNT(vc) > 0 THEN true ELSE false END " +
            "FROM VerificacionCliente vc " +
            "WHERE vc.cliente.id = :clienteId " +
            "AND vc.activo = true")
    boolean existsVerificacionActivaByClienteId(@Param("clienteId") Long clienteId);

}
