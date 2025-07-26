package org.example.zarp_back.model.dto.detalleImagenPropiedad;

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

    private Boolean imgPrincipal;
    private ImagenDTO imagen;
    private Long propiedadId;
}
