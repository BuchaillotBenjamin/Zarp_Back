package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Usuario extends Base{

    private String uid;
    private String nombreCompleto;
    private String correoElectronico;


}
