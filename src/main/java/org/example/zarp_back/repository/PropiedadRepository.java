package org.example.zarp_back.repository;

import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.Provincia;
import org.example.zarp_back.model.enums.VerificacionPropiedad;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropiedadRepository extends GenericoRepository<Propiedad, Long> {

    List<Propiedad> findByDireccionProvinciaAndActivoAndVerificacionPropiedad(Provincia provincia, boolean activo, VerificacionPropiedad verificacionPropiedad);
    List<Propiedad> findByClienteId(Long idCliente);

}
