package org.example.zarp_back.model.dto.ambiente;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbienteDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;

}
