package org.example.zarp_back.model.dto.detalleImagenPropiedad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleImagenPropiedadDTO {
    @NotNull(message = "El campo imgPrincipal no puede ser nulo")
    private Boolean imgPrincipal;
    @Valid
    @NotNull(message = "El campo imagen no puede ser nulo")
    private ImagenDTO imagen;
    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser mayor a cero")
    private Long propiedadId;
}
