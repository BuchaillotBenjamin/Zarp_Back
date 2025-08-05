package org.example.zarp_back.model.dto.empleado;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.zarp_back.model.dto.direccion.DireccionDTO;
import org.example.zarp_back.model.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {

    @JsonProperty("uid")
    @NotBlank(message = "El campo uId no puede estar vacio")
    private String uid;
    @NotBlank(message = "El campo nombreCompleto no puede estar vacio")
    private String nombreCompleto;
    @NotBlank(message = "El campo correoElectronico no puede estar vacio")
    private String correoElectronico;
    @NotNull(message = "El campo rol no puede ser nulo")
    private Rol rol;


}
