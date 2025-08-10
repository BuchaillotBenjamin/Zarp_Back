package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends GenericoRepository<Reserva, Long> {


    boolean existsByClienteIdAndPropiedadIdAndEstado(Long clienteId, Long propiedadId, Estado estado);
    @Query("""
    SELECT r 
    FROM Reserva r 
    WHERE r.propiedad.id = :propiedadId
      AND r.activo = true
      AND r.estado NOT IN (:estado1, :estado2)
""")
    List<Reserva> findReservasActivasPorPropiedad(
            @Param("propiedadId") Long propiedadId,
            @Param("estado1") Estado estado1,
            @Param("estado2") Estado estado2
    );
}
