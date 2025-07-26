package org.example.zarp_back.model.dto.cliente;

import lombok.*;
import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.entity.Usuario;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private UsuarioDTO usuario;
    private String telefono;
    private Boolean correoVerificado;
    private Boolean documentoVerificado;

}
