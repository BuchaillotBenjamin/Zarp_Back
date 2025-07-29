package org.example.zarp_back.model.dto.propiedad;

import lombok.*;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteResponseDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaResponseDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadResponseDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaResponseDTO;
import org.example.zarp_back.model.dto.direccion.DireccionResponseDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaResponseDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadResponseDTO;
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
    //private ClienteResponseDTO propietario;
    private Double precioPorNoche;
    private VerificacionPropiedad verificacionPropiedad;
    private DireccionResponseDTO direccion;
    private TipoPropiedadResponseDTO tipoPropiedad;
    private List<ReseniaResponseDTO> resenias;
    private List<DetalleTipoPersonaResponseDTO> detalleTipoPersonas;
    private List<DetalleCaracteristicaResponseDTO> detalleCaracteristicas;
    private List<DetalleImagenPropiedadResponseDTO> detalleImagenes;
    private List<DetalleAmbienteResponseDTO> detalleAmbientes;
}
