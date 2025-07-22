package org.example.zarp_back.model.dto.mensaje;

import lombok.*;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Conversacion;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeDTO {

    private String contenido;
    private Long emisorId;
    private Long receptorId;
    private Long conversacionId;

}
