package org.example.zarp_back.model.dto.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private String nombreCompleto;
    private String correoElectronico;
    private String contrasena;

}
