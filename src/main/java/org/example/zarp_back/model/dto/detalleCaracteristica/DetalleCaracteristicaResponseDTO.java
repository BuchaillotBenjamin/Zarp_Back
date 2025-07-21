package org.example.zarp_back.model.dto.detalleCaracteristica;

import lombok.*;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCaracteristicaResponseDTO {


    private Long id;
    private Caracteristica caracteristica;

}
