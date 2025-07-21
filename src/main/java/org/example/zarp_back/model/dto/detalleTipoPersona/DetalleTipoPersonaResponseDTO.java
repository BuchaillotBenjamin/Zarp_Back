package org.example.zarp_back.model.dto.detalleTipoPersona;

import lombok.*;

import org.example.zarp_back.model.entity.TipoPersona;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleTipoPersonaResponseDTO {

    private Long id;
    private Integer Cantidad;
    private TipoPersona tipoPersona;


}
