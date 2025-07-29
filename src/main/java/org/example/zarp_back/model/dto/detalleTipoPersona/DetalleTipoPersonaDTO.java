package org.example.zarp_back.model.dto.detalleTipoPersona;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.TipoPersona;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleTipoPersonaDTO {

    @NotNull(message = "El campo cantidad no puede ser nulo")
    @Positive(message = "El campo cantidad debe ser mayor a cero")
    private Integer cantidad;
    @NotNull(message = "El campo tipoPersonaId no puede ser nulo")
    @Positive(message = "El campo tipoPersonaId debe ser mayor a cero")
    private Long tipoPersonaId;
    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser mayor a cero")
    private Long propiedadId;
}
