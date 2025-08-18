package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends GenericoRepository<Cliente, Long> {

    boolean existsByUid(String uid);

    Cliente findByUid(String uid);

}
