package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Empleado;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends GenericoRepository<Empleado, Long> {

}
