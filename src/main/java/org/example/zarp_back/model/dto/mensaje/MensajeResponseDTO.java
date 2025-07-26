package org.example.zarp_back.model.dto.mensaje;

import lombok.*;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajeResponseDTO {

    private Long id;
    private String contenido;
    private ClienteResponseDTO emisor;
    private ClienteResponseDTO receptor;
    private LocalDate fechaEnvio;
    private LocalTime horaEnvio;

}
