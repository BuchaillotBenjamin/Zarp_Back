package org.example.zarp_back.model.dto.tipoPersona;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPersonaResponseDTO {
    private Long id;
    private Boolean activo;
    private String denominacion;
    private String descripcion;

}
