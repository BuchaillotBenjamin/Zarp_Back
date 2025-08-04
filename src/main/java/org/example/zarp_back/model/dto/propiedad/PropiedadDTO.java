package org.example.zarp_back.model.dto.propiedad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.direccion.DireccionDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadDTO {

    @NotBlank(message = "El campo nombre no puede estar vacio")
    private String nombre;
    @NotBlank(message = "El campo descripcion no puede estar vacio")
    private String descripcion;
    @NotNull(message = "El campo precioPorNoche no puede ser nulo")
    @Positive(message = "El campo precioPorNoche debe ser un valor positivo")
    private Double precioPorNoche;
    @Valid
    @NotNull(message = "El campo cantidadHuespedes no puede ser nulo")
    private DireccionDTO direccion;
    @NotNull(message = "El campo propietarioId no puede ser nulo")
    @Positive(message = "El campo propietarioId debe ser un valor positivo")
    private Long propietarioId;
    @NotNull(message = "El campo tipoPropiedadId no puede ser nulo")
    @Positive(message = "El campo tipoPropiedadId debe ser un valor positivo")
    private Long tipoPropiedadId;
    @Valid
    @NotNull(message = "El campo detalleTipoPersonas no puede ser nulo")
    @Size(min = 1, message = "El campo detalleTipoPersonas debe contener por lo menos 1 elemento")
    private List<DetalleTipoPersonaDTO> detalleTipoPersonas;
    @Valid
    @NotNull(message = "El campo detalleCaracteristicas no puede ser nulo")
    @Size(min = 1, message = "El campo detalleCaracteristicas debe contener por lo menos 1 elemento")
    private List<DetalleCaracteristicaDTO> detalleCaracteristicas;
    @Valid
    @NotNull(message = "El campo detalleImagenes no puede ser nulo")
    @Size(min = 1, message = "El campo detalleImagenes debe contener por lo menos 1 elemento")
    private List<DetalleImagenPropiedadDTO> detalleImagenes;
    @Valid
    @NotNull(message = "El campo detalleResenias no puede ser nulo")
    @Size(min = 1, message = "El campo detalleResenias debe contener por lo menos 1 elemento")
    private List<DetalleAmbienteDTO> detalleAmbientes;

}
