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
public class DetalleTipoPersona extends Base{

    private Integer cantidad;
    @ManyToOne
    @JoinColumn(name = "tipo_persona_id")
    private TipoPersona tipoPersona;
    @ManyToOne
    @JoinColumn(name = "datos_propiedad_id")
    private Propiedad propiedad;

}
