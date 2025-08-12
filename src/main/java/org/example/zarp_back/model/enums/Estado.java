package org.example.zarp_back.model.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Estado {
    PENDIENTE,
    RESERVADA,
    ACTIVA,
    FINALIZADA,
    CANCELADA;


    public Set<Estado> getEstadosPermitidos() {
        return switch (this) {
            case PENDIENTE -> EnumSet.of(RESERVADA, CANCELADA);
            case RESERVADA -> EnumSet.of(ACTIVA, CANCELADA);
            case ACTIVA -> EnumSet.of(FINALIZADA);
            case FINALIZADA, CANCELADA -> EnumSet.noneOf(Estado.class);
        };
    }

    public boolean puedeTransicionarA(Estado nuevoEstado) {
        return getEstadosPermitidos().contains(nuevoEstado);
    }

}
