package org.example.zarp_back.model.dto.tipoPropiedad;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPropiedadDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;
}
