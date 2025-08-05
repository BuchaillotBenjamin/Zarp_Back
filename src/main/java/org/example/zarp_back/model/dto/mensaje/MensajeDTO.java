package org.example.zarp_back.model.dto.mensaje;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Conversacion;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDTO {

    @NotBlank(message = "El campo contenido no puede estar vacio")
    private String contenido;
    @NotNull(message = "El campo emisor no puede ser nulo")
    @Positive(message = "El campo emisor debe ser un valor positivo")
    private Long emisorId;
}
