package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificacionClienteRepository extends GenericoRepository<VerificacionCliente, Long> {

}
