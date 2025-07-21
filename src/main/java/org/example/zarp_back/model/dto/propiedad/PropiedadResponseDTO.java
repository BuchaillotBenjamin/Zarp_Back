package org.example.zarp_back.model.dto.propiedad;

import lombok.*;
import org.example.zarp_back.model.entity.*;
import org.example.zarp_back.model.enums.VerificacionPropiedad;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadResponseDTO {

    private Long id;
    private Boolean activo;
    private String nombre;
    private String descripcion;
    private Double precioPorNoche;
    private VerificacionPropiedad verificacionPropiedad;
    private Direccion direccion;
    private TipoPropiedad tipoPropiedad;
    private List<Resenia> resenias;
    private List<DetalleTipoPersona> detalleTipoPersonas;
    private List<DetalleCaracteristica> detalleCaracteristicas;
    private List<DetalleImagenPropiedad> detalleImagenes;
    private List<DetalleAmbiente> detalleAmbientes;
}
