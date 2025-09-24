package org.example.zarp_back.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.zarp_back.model.enums.VerificacionPropiedad;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Propiedad extends Base {

    private String nombre;
    @Column(length = 1000)
    private String descripcion;
    private Double precioPorNoche;
    private VerificacionPropiedad verificacionPropiedad;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Direccion direccion;

    @ManyToOne
    private TipoPropiedad tipoPropiedad;

    @ManyToOne
    private Cliente propietario;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resenia> resenias;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleTipoPersona> detalleTipoPersonas;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCaracteristica> detalleCaracteristicas;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleImagenPropiedad> detalleImagenes;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleAmbiente> detalleAmbientes;
}
