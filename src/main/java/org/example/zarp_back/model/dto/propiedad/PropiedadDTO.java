package org.example.zarp_back.model.dto.propiedad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.example.zarp_back.model.entity.*;
import org.example.zarp_back.model.enums.VerificacionPropiedad;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadDTO {

    private String nombre;
    private String descripcion;
    private Double precioPorNoche;
    private VerificacionPropiedad verificacionPropiedad;
    private Direccion direccion;
    private Long tipoPropiedadId;
    private List<Resenia> resenias;
    private List<DetalleTipoPersona> detalleTipoPersonas;
    private List<DetalleCaracteristica> detalleCaracteristicas;
    private List<DetalleImagenPropiedad> detalleImagenes;
    private List<DetalleAmbiente> detalleAmbientes;

}
