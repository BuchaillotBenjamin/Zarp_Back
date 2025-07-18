/*
package org.example.zarp_back.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class DatosPropiedad extends Base {


    @OneToMany(mappedBy = "datosPropiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleTipoPersona> detalleTipoPersonas;

    @OneToMany(mappedBy = "datosPropiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCaracteristica> detalleCaracteristicas;

    @OneToMany(mappedBy = "datosPropiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleImagenPropiedad> detalleImagenes;

    @OneToMany(mappedBy = "datosPropiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleAmbiente> detalleAmbientes;
}
*/
