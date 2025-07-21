package org.example.zarp_back.model.dto.mensaje;

import lombok.*;
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
    private Cliente emisor;
    private Cliente receptor;
    private LocalDate fechaEnvio;
    private LocalTime horaEnvio;

}
