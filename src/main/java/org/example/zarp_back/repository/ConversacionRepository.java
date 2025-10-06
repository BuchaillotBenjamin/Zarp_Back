package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Conversacion;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversacionRepository extends GenericoRepository<Conversacion, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Conversacion c
        WHERE (c.cliente1.id = :clienteId1 AND c.cliente2.id = :clienteId2)
           OR (c.cliente1.id = :clienteId2 AND c.cliente2.id = :clienteId1)
    """)
    boolean existsByClienteIds(@Param("clienteId1") Long clienteId1, @Param("clienteId2") Long clienteId2);

    @Query("SELECT c FROM Conversacion c WHERE c.cliente1.id = :clienteId OR c.cliente2.id = :clienteId")
    List<Conversacion> findByClienteId(@Param("clienteId") Long clienteId);




}
