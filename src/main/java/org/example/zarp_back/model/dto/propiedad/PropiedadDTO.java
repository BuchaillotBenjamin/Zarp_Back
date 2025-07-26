package org.example.zarp_back.model.dto.propiedad;

import lombok.*;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaDTO;
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
    private Direccion direccion;
    private Long tipoPropiedadId;
    private List<ReseniaDTO> resenias;
    private List<DetalleTipoPersonaDTO> detalleTipoPersonas;
    private List<DetalleCaracteristicaDTO> detalleCaracteristicas;
    private List<DetalleImagenPropiedadDTO> detalleImagenes;
    private List<DetalleAmbienteDTO> detalleAmbientes;

}
