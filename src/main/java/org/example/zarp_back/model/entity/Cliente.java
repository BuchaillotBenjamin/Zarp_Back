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
public class Cliente extends Base {

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;
    private String telefono;
    private Boolean correoVerificado;
    private Boolean documentoVerificado;

}
