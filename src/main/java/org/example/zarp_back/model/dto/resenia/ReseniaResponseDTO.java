package org.example.zarp_back.model.dto.resenia;


import lombok.*;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReseniaResponseDTO {

    private Long id;
    private ClienteResponseDTO usuario;
    private String comentario;
    private Integer calificacion;

}
