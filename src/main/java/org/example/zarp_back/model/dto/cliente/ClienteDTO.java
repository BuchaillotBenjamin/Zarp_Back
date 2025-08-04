package org.example.zarp_back.model.dto.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.zarp_back.model.dto.usuario.UsuarioDTO;
import org.example.zarp_back.model.entity.Usuario;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    @Valid
    @NotNull(message = "El campo usuario no puede ser nulo")
    private UsuarioDTO usuario;

}
