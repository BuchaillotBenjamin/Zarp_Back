package org.example.zarp_back.model.dto.reseña;



import lombok.*;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaDTO {

    private Propiedad propiedad;
    private Long usuarioId;
    private String comentario;
    private Integer calificacion;
}
