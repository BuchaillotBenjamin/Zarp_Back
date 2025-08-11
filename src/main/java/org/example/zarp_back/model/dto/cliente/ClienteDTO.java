package org.example.zarp_back.model.dto.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.zarp_back.model.dto.imagen.ImagenDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    @JsonProperty("uid")
    @NotBlank(message = "El campo uId no puede estar vacio")
    private String uid;
    @NotBlank(message = "El campo nombreCompleto no puede estar vacio")
    private String nombreCompleto;
    @NotBlank(message = "El campo correoElectronico no puede estar vacio")
    private String correoElectronico;
    @NotNull(message = "El campo fotoPerfil no puede ser nulo")
    private ImagenDTO fotoPerfil;

}
