package org.example.zarp_back.model.dto.reserva;

import lombok.*;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
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
public class ReservaResponseDTO {

    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double precioTotal;
    private ClienteResponseDTO cliente;
    private PropiedadResponseDTO propiedad;
    private Estado estado;
    private FormaPago formaPago;

}
