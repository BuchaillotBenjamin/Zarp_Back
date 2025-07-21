package org.example.zarp_back.model.dto.detalleTipoPersona;

import lombok.*;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.TipoPersona;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleTipoPersonaDTO {

    private Integer Cantidad;
    private TipoPersona tipoPersona;
    private Propiedad propiedad;
}
