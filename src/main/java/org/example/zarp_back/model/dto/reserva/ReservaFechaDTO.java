package org.example.zarp_back.model.dto.reserva;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaFechaDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
