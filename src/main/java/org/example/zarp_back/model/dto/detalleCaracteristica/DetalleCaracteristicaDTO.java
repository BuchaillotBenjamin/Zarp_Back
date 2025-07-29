package org.example.zarp_back.model.dto.detalleCaracteristica;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCaracteristicaDTO {

    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser mayor a cero")
    private Long propiedadId;

    @NotNull(message = "El campo caracteristicaId no puede ser nulo")
    @Positive(message = "El campo caracteristicaId debe ser mayor a cero")
    private Long caracteristicaId;
}
