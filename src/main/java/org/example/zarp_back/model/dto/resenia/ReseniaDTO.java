package org.example.zarp_back.model.dto.resenia;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaDTO {

    @NotNull(message = "El campo propiedadId no puede ser nulo")
    @Positive(message = "El campo propiedadId debe ser un valor positivo")
    private Long propiedadId;
    @NotNull(message = "El campo usuarioId no puede ser nulo")
    @Positive(message = "El campo usuarioId debe ser un valor positivo")
    private Long usuarioId;
    @NotBlank(message = "El campo comentario no puede estar vacio")
    private String comentario;
    @Min(value = 1, message = "La calificación mínima permitida es 1")
    @Max(value = 5, message = "La calificación máxima permitida es 10")
    @NotNull(message = "El campo calificación no puede ser nulo")
    private Integer calificacion;
}
