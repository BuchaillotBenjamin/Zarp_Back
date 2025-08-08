package org.example.zarp_back.model.dto.direccion;

import lombok.*;
import org.example.zarp_back.model.enums.Provincia;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionResponseDTO {

    private Long id;
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
