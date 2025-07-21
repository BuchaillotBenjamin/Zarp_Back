package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Resenia extends Base {

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Cliente usuario;
    private String comentario;
    private Integer calificacion;
}
