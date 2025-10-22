package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.zarp_back.model.enums.EstadoPagosPendientes;
import org.example.zarp_back.model.enums.FormaPago;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class PagoPendiente extends Base{

    private Double monto;
    @ManyToOne
    private Cliente propietario;
    private LocalDateTime fechaCreacion;
    private FormaPago formaPago;
    private EstadoPagosPendientes estadoPagosPendientes;
}
