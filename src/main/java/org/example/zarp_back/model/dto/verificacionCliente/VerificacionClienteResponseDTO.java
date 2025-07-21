package org.example.zarp_back.model.dto.verificacionCliente;

import lombok.*;
import org.example.zarp_back.model.entity.Imagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificacionClienteResponseDTO {

    private Long id;
    private Boolean activo;
    private Imagen fotoFrontal;
    private Imagen fotoDocumentoFrontal;
    private Imagen fotoDocumentoTrasero;

}
