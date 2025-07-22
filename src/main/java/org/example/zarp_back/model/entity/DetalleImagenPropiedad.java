package org.example.zarp_back.model.entity;

import jakarta.persistence.CascadeType;
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
public class DetalleImagenPropiedad extends Base{

    private Boolean imgPrincipal;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;
    @ManyToOne
    @JoinColumn(name = "datos_propiedad_id")
    private Propiedad propiedad;
}
