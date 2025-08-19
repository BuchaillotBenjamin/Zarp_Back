package org.example.zarp_back.model.dto.cliente;

import lombok.*;
import org.example.zarp_back.model.dto.imagen.ImagenResponseDTO;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {

    private Long id;
    private Boolean activo;
    private String uid;
    private String nombreCompleto;
    private String correoElectronico;
    private Rol rol;
    private Boolean correoVerificado;
    private Boolean documentoVerificado;
    private ImagenResponseDTO fotoPerfil;

}
