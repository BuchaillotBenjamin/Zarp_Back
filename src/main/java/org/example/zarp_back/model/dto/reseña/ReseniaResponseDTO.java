package org.example.zarp_back.model.dto.reseña;


import lombok.*;
import org.example.zarp_back.model.entity.Cliente;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaResponseDTO {

    private Long id;
    private Cliente usuario;
    private String comentario;
    private Integer calificacion;

}
