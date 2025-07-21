package org.example.zarp_back.model.dto.conversacion;

import lombok.*;
import org.example.zarp_back.model.entity.Mensaje;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversacionResponseDTO {

    private Long id;
    private Boolean activo;
    private List<Mensaje> mensajes;
    private LocalDate fechaCreacion;

}
