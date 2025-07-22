package org.example.zarp_back.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericoRepository <T, ID extends Serializable>  extends JpaRepository<T, ID> {



}
