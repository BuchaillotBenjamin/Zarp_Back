package org.example.zarp_back.model.dto.reserva;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.config.validation.RangoFechaValido;
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
@RangoFechaValido
public class ReservaDTO {

    @NotNull(message = "El fechaInicio estado no puede ser nulo")
    private LocalDate fechaInicio;
    @NotNull(message = "El campo fechaFin no puede ser nulo")
    private LocalDate fechaFin;
    @NotNull(message = "El campo precioTotal no puede ser nulo")
    @Positive(message = "El campo precioTotal debe ser un valor positivo")
    private Double precioTotal;
    @NotNull(message = "El campo clienteId no puede ser nulo")
    @Positive(message = "El campo clienteId debe ser un valor positivo")
    private Long clienteId;
    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser un valor positivo")
    private Long propiedadId;
    @NotNull(message = "El campo formaPago no puede ser nulo")
    private FormaPago formaPago;

}
