package org.example.zarp_back.model.dto.detalleAmbiente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAmbienteDTO {

    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Positive(message = "El campo cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser mayor a cero")
    private Long propiedadId;

    @NotNull(message = "El campo ambienteId no puede ser nulo")
    @Positive(message = "El campo ambienteId debe ser mayor a cero")
    private Long ambienteId;

}
