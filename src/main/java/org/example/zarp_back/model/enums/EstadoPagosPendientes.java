package org.example.zarp_back.model.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EstadoPagosPendientes {

    PENDIENTE {
        @Override
        public EstadoPagosPendientes siguiente() {
            return INICIADO;
        }
    },
    INICIADO {
        @Override
        public EstadoPagosPendientes siguiente() {
            return COMPLETADO;
        }
    },
    COMPLETADO{
        @Override
        public EstadoPagosPendientes siguiente() {
            log.info("El estado COMPLETADO no tiene un siguiente estado.");
            return null;
        }
    };

    public abstract EstadoPagosPendientes siguiente();

    public boolean puedeTransicionarA(EstadoPagosPendientes siguienteEstado) {
        try {
            return this.siguiente() == siguienteEstado;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}

