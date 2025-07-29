package org.example.zarp_back.model.dto.empleado;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    @NotNull(message = "El campo usuario no puede ser nulo")
    private UsuarioDTO usuario;
    @NotNull(message = "El campo direccion no puede ser nulo")
    private DireccionDTO direccion;
    @NotBlank(message = "El campo telefono no puede estar vacío")
    private String telefono;


}
