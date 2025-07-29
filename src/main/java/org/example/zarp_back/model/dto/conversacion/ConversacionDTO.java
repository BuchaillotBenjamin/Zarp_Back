package org.example.zarp_back.model.dto.conversacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.zarp_back.model.dto.mensaje.MensajeDTO;
import org.example.zarp_back.model.entity.Mensaje;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversacionDTO {

    @Valid
    @NotNull(message = "El campo mensajes no puede ser nulo")
    @Size(min = 1, message = "El campo mensajes debe contener al menos un mensaje")
    private List<MensajeDTO> mensajes;

}
