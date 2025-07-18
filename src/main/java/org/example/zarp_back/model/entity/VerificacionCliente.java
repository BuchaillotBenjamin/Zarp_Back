package org.example.zarp_back.model.entity;

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

    @OneToOne
    private Imagen fotoFrontal;
    @OneToOne
    private Imagen fotoDocumentoFrontal;
    @OneToOne
    private Imagen fotoDocumentoTrasero;

}
