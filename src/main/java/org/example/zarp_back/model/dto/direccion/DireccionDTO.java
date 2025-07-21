package org.example.zarp_back.model.dto.direccion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    private String calle;
    private String numero;
    private String piso;
    private String departamento;
    private String codigoPostal;
    private String localidad;
    private Double latitud;
    private Double longitud;

}
