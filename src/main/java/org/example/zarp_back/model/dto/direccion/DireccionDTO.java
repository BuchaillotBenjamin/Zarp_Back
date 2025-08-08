package org.example.zarp_back.model.dto.direccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.zarp_back.model.enums.Provincia;

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
    @NotNull(message = "El campo provincia no puede ser nulo")
    private Provincia provincia;
    @NotNull(message = "El campo latitud no puede ser nulo")
    private Double latitud;
    @NotNull(message = "El campo longitud no puede ser nulo")
    private Double longitud;

}
