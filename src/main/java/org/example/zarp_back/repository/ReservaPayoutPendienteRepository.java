package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.ReservaPayoutPendiente;
import org.example.zarp_back.model.interfaces.GenericoRepository;

import java.util.List;

public interface ReservaPayoutPendienteRepository extends GenericoRepository<ReservaPayoutPendiente, Long> {


    List<ReservaPayoutPendiente> findByActivoTrue();

    ReservaPayoutPendiente findByReservaId(Long reservaId);


}
