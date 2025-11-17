package org.example.zarp_back.model.dto.credencialesMP;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredencialesMPDTO {

    @NotBlank(message = "El CVU no puede estar vacío")
    private String cvu;
    @NotBlank(message = "El nombre del titular no puede estar vacío")
    private String nombreTitular;

}
