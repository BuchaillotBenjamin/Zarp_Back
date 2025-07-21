package org.example.zarp_back.model.dto.cliente;

import lombok.*;
import org.example.zarp_back.model.entity.Usuario;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {

    private Long id;
    private Boolean activo;
    private Usuario usuario;
    private String telefono;
    private Boolean correoVerificado;
    private Boolean documentoVerificado;

}
