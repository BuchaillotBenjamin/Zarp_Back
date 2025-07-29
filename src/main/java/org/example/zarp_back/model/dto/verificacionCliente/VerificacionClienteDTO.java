package org.example.zarp_back.model.dto.verificacionCliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Imagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificacionClienteDTO {

    @Valid
    @NotNull(message = "El campo fotoFrontal no puede estar vacio")
    private Imagen fotoFrontal;
    @Valid
    @NotNull(message = "El campo fotoDocumentoFrontal no puede estar vacio")
    private Imagen fotoDocumentoFrontal;
    @Valid
    @NotNull(message = "El campo fotoDocumentoTrasero no puede estar vacio")
    private Imagen fotoDocumentoTrasero;
    @NotNull(message = "El campo clienteId no puede estar vacio")
    @Positive(message = "El campo clienteId debe ser un número positivo")
    private Long clienteId;

}
