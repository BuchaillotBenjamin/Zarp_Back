package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.zarp_back.model.enums.Provincia;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Direccion extends Base {

    private String calle;
    private String numero;
    private String piso;
    private String departamento;
    private String codigoPostal;
    private String localidad;
    private Provincia provincia;
    private Double latitud;
    private Double longitud;


}
