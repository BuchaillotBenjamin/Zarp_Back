package org.example.zarp_back.model.dto.pagosPendientes;

import lombok.*;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.dto.empleado.EmpleadoResponseDTO;
import org.example.zarp_back.model.enums.EstadoPagosPendientes;
import org.example.zarp_back.model.enums.FormaPago;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoPendienteResponseDTO {
    private Long id;
    private Boolean activo;
    private Double monto;
    private ClienteResponseDTO propietario;
    private LocalDateTime fechaCreacion;
    private FormaPago formaPago;
    private EstadoPagosPendientes estadoPagosPendientes;
    private EmpleadoResponseDTO empleado;
}
