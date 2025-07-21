package org.example.zarp_back.model.dto.imagen;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenResponseDTO {
    private Long id;
    private String urlImagen;

}
