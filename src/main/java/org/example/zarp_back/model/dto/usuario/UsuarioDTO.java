package org.example.zarp_back.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    @JsonProperty("uid")
    @NotBlank(message = "El campo uId no puede estar vacio")
    private String uid;
    @NotBlank(message = "El campo nombreCompleto no puede estar vacio")
    private String nombreCompleto;
    @NotBlank(message = "El campo correoElectronico no puede estar vacio")
    private String correoElectronico;
    /*@NotBlank(message = "El campo contrasena no puede estar vacio")
    private String contrasena;*/

}
