package org.example.zarp_back.model.dto.empleado;

import lombok.*;
import org.example.zarp_back.model.dto.direccion.DireccionResponseDTO;
import org.example.zarp_back.model.dto.usuario.UsuarioResponseDTO;
import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoResponseDTO {

    private Long id;
    private Boolean activo;
    private UsuarioResponseDTO usuario;
    private Rol rol;
    private DireccionResponseDTO direccion;
    private String telefono;

}
