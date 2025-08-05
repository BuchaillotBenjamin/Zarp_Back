package org.example.zarp_back.model.dto.empleado;

import lombok.*;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoResponseDTO {

    private Long id;
    private Boolean activo;
    private String uid;
    private String nombreCompleto;
    private String correoElectronico;
    private Rol rol;

}
