package org.example.zarp_back.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
public class VerificacionCliente extends Base {

    @OneToOne(cascade = CascadeType.PERSIST)
    private Imagen fotoFrontal;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Imagen fotoDocumentoFrontal;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Imagen fotoDocumentoTrasero;

}
