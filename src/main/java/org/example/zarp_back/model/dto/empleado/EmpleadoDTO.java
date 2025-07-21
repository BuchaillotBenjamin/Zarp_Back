package org.example.zarp_back.model.dto.empleado;


import lombok.*;
import org.example.zarp_back.model.entity.Direccion;
import org.example.zarp_back.model.entity.Usuario;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {

    private Usuario usuario;
    private Rol rol;
    private Direccion direccion;
    private String telefono;


}
