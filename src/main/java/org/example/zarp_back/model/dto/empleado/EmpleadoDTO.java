package org.example.zarp_back.model.dto.empleado;


import lombok.*;
import org.example.zarp_back.model.dto.direccion.DireccionDTO;
import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {

    private UsuarioDTO usuario;
    private Rol rol;
    private DireccionDTO direccion;
    private String telefono;


}
