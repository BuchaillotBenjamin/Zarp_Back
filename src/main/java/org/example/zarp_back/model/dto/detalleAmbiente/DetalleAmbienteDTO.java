package org.example.zarp_back.model.dto.detalleAmbiente;

import lombok.*;
import org.example.zarp_back.model.entity.Ambiente;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAmbienteDTO {

    private Integer cantidad;
    private Propiedad propiedad;
    private Ambiente ambiente;

}
