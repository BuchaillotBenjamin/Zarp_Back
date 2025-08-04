package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.interfaces.GenericoRepository;

public interface ReservaRepository extends GenericoRepository<Reserva, Long> {


    boolean existsByClienteIdAndPropiedadIdAndEstado(Long clienteId, Long propiedadId, Estado estado);

}
