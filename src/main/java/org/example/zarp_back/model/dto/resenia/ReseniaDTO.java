package org.example.zarp_back.model.dto.resenia;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.zarp_back.model.entity.Propiedad;

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
    @NotNull(message = "El campo calificacion no puede ser nulo")
    @Positive(message = "El campo calificacion debe ser un valor positivo")
    private Integer calificacion;
}
