package org.example.zarp_back.model.dto.reserva;

import lombok.*;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.FormaPago;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;
    private Long clienteId;
    private Long propiedadId;
    private FormaPago formaPago;

}
