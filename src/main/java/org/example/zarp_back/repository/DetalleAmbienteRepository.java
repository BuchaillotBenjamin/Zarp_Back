package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.DetalleAmbiente;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleAmbienteRepository extends GenericoRepository<DetalleAmbiente, Long> {

    // Aquí puedes agregar métodos específicos para DetalleAmbiente si es necesario
    // Por ejemplo, si necesitas buscar por algún campo específico, puedes definirlo aquí.
}
