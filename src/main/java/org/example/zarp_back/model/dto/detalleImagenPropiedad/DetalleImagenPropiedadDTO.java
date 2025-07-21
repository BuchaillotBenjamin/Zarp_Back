package org.example.zarp_back.model.dto.detalleImagenPropiedad;

import lombok.*;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleImagenPropiedadDTO {

    private Boolean imgPrincipal;
    private Imagen imagen;
    private Propiedad propiedad;
}
