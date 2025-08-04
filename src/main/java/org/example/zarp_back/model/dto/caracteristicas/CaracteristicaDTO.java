package org.example.zarp_back.model.dto.caracteristicas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.entity.Imagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristicaDTO {

    @NotBlank(message = "El campo denominacion no puede estar vacio")
    private String denominacion;
    @NotBlank(message = "El campo descripcion no puede estar vacio")
    private String descripcion;
    @Valid
    @NotNull(message = "El campo imagen no puede estar vacio")
    private ImagenDTO imagen;

}
