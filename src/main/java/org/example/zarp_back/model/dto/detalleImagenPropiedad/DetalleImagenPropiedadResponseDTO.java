package org.example.zarp_back.model.dto.detalleImagenPropiedad;

import lombok.*;
import org.example.zarp_back.model.dto.imagen.ImagenResponseDTO;
import org.example.zarp_back.model.entity.Imagen;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleImagenPropiedadResponseDTO {

    private Long id;
    private Boolean imgPrincipal;
    private ImagenResponseDTO imagen;


}
