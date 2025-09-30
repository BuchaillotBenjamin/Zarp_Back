package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ReservaRepository extends GenericoRepository<Reserva, Long> {


    boolean existsByClienteIdAndPropiedadIdAndEstado(Long clienteId, Long propiedadId, Estado estado);
    @Query("""
    SELECT r 
    FROM Reserva r 
    WHERE r.propiedad.id = :propiedadId
      AND r.activo = true
      AND r.estado NOT IN (org.example.zarp_back.model.enums.Estado.CANCELADA,
                              org.example.zarp_back.model.enums.Estado.FINALIZADA)
    """)
    List<Reserva> findReservasActivasPorPropiedad(@Param("propiedadId") Long propiedadId);

    @Query("""
        SELECT r
        FROM Reserva r
        WHERE r.activo = true
          AND r.estado NOT IN (org.example.zarp_back.model.enums.Estado.CANCELADA,
                              org.example.zarp_back.model.enums.Estado.FINALIZADA)
    """)
    List<Reserva> findReservasActivas();

    @Query("""
    SELECT r FROM Reserva r
    WHERE r.propiedad.id = :propiedadId
      AND r.activo = true
      AND r.estado NOT IN (org.example.zarp_back.model.enums.Estado.CANCELADA,
                           org.example.zarp_back.model.enums.Estado.FINALIZADA)
      AND (
            (:inicio BETWEEN r.fechaInicio AND r.fechaFin)
         OR (:fin BETWEEN r.fechaInicio AND r.fechaFin)
         OR (r.fechaInicio BETWEEN :inicio AND :fin)
         OR (r.fechaFin BETWEEN :inicio AND :fin)
      )
""")
    List<Reserva> findReservasSolapadas(
            @Param("propiedadId") Long propiedadId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

}
