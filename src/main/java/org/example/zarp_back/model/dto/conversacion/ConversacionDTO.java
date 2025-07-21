package org.example.zarp_back.model.dto.conversacion;

import lombok.*;
import org.example.zarp_back.model.entity.Mensaje;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversacionDTO {

    private List<Mensaje> mensajes;

}
