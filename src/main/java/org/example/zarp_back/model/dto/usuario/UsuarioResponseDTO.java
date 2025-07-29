package org.example.zarp_back.model.dto.usuario;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String uId;
    private Boolean activo;
    private String nombreCompleto;
    private String correoElectronico;

}
