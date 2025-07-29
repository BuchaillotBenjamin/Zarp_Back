package org.example.zarp_back.model.dto.resenia;



import lombok.*;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaDTO {

    private Long propiedadId;
    private Long usuarioId;
    private String comentario;
    private Integer calificacion;
}
