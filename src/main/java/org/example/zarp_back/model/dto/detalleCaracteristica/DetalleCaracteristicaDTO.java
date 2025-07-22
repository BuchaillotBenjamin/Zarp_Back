package org.example.zarp_back.model.dto.detalleCaracteristica;


import lombok.*;
import org.example.zarp_back.model.entity.Caracteristica;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCaracteristicaDTO {

    private Long propiedadId;
    private Long caracteristicaId;

}
