package org.example.zarp_back.model.dto.detalleAmbiente;

import lombok.*;
import org.example.zarp_back.model.entity.Ambiente;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleAmbienteResponseDTO {

    private Long id;
    private Integer cantidad;
    private Ambiente ambiente;

}
