package org.example.zarp_back.model.dto.tipoPropiedad;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPropiedadResponseDTO {

    private Long id;
    private Boolean activo;
    private String denominacion;

}
