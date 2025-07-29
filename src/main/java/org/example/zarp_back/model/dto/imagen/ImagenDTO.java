package org.example.zarp_back.model.dto.imagen;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenDTO {

    @NotBlank(message = "El campo urlImagen no puede estar vacio")
    private String urlImagen;

}
