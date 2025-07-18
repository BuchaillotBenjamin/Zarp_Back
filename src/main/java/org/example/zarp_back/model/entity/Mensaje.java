package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Mensaje extends Base{

    private String contenido;
    @ManyToOne
    private Cliente emisor;
    @ManyToOne
    private Cliente receptor;
    @ManyToOne
    private Conversacion conversacion;
    private LocalDate fechaEnvio;
    private LocalTime hora;

}
