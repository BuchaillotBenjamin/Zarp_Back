package org.example.zarp_back.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Conversacion extends Base{

    @OneToMany(mappedBy = "conversacion",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes;

    private LocalDate fechaCreacion;
}
