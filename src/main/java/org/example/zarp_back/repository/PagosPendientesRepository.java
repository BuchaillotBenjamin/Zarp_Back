package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.PagoPendiente;
import org.example.zarp_back.model.enums.EstadoPagosPendientes;
import org.example.zarp_back.model.interfaces.GenericoRepository;

import java.util.List;

public interface PagosPendientesRepository extends GenericoRepository<PagoPendiente, Long> {

    public List<PagoPendiente> findByActivoTrue ();
    public List<PagoPendiente> findByEstadoPagosPendientes(EstadoPagosPendientes estado);
}
