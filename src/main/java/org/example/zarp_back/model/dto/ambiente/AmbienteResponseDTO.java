package org.example.zarp_back.model.dto.ambiente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbienteResponseDTO {

    private Long id;
    private String denominacion;
    private Boolean activo;
}
