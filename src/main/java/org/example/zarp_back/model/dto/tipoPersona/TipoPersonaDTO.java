package org.example.zarp_back.model.dto.tipoPersona;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPersonaDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;
    @NotBlank(message = "El campo descripcion no puede estar vacio")
    private String descripcion;


}
