package org.example.zarp_back.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Base implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Boolean activo = true;
}