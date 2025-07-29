package org.example.zarp_back.model.dto.direccion;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    @NotBlank(message = "El campo calle no puede estar vacío")
    private String calle;
    @NotBlank(message = "El campo numero no puede estar vacío")
    private String numero;
    @NotBlank(message = "El campo piso no puede estar vacío")
    private String piso;
    @NotBlank(message = "El campo departamento no puede estar vacío")
    private String departamento;
    @NotBlank(message = "El campo codigoPostal no puede estar vacío")
    private String codigoPostal;
    @NotBlank(message = "El campo localidad no puede estar vacío")
    private String localidad;
    @NotBlank(message = "El campo latitud no puede estar vacío")
    private Double latitud;
    @NotBlank(message = "El campo longitud no puede estar vacío")
    private Double longitud;

}
