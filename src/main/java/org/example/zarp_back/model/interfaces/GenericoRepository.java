package org.example.zarp_back.model.interfaces;

import org.example.zarp_back.model.entity.VerificacionCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface GenericoRepository <T, ID extends Serializable>  extends JpaRepository<T, ID> {

    List<T> findByActivo(boolean activo);

}
