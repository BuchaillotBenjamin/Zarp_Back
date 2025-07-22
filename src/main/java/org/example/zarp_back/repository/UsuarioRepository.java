package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends GenericoRepository<Usuario, Long> {

}
