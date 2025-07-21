package org.example.zarp_back.model.dto.caracteristicas;

import lombok.*;

import org.example.zarp_back.model.entity.Imagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristicaDTO {

    private String denominacion;
    private String descripcion;
    private Imagen imagen;

}
