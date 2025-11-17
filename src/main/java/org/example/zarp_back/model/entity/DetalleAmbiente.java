package org.example.zarp_back.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class DetalleAmbiente extends Base{

    private Integer cantidad;
    @ManyToOne
    @JsonBackReference
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "ambiente_id")
    private Ambiente ambiente;

}
