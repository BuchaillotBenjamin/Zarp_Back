package org.example.zarp_back.model.dto.caracteristicas;

import lombok.*;
import org.example.zarp_back.model.entity.Imagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristicaResponseDTO {

    private Long id;
    private Boolean activo;
    private String denominacion;
    private String descripcion;
    private Imagen imagen;

}
