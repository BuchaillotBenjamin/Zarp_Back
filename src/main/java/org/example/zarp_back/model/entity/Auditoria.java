package org.example.zarp_back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid; // UID del usuario autenticado

    private String entidad; // Ej: "Pago", "Cliente", "Usuario"

    private String accion; // Ej: "save", "delete", "update", "toggleActivo"

    private String entidadId; // ID del recurso afectado (como String para flexibilidad)

    private LocalDateTime timestamp;

}
