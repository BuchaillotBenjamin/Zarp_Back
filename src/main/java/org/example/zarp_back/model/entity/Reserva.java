package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.zarp_back.config.validation.RangoFechaValido;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.FormaPago;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Reserva extends Base {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double precioTotal;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
    private Estado estado;
    private FormaPago formaPago;


}
