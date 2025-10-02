package org.example.zarp_back.service.utils;

import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservaSchedulerTest {

    @Autowired
    private ReservaScheduler reservaScheduler;

    @MockitoBean
    private ReservaRepository reservaRepository;

    @Test
    void actualizarEstadosReservas_actualizaCorrectamente() {
        LocalDate hoy = LocalDate.now();

        // Reserva que debería pasar a ACTIVA
        Reserva reservaActiva = new Reserva();
        reservaActiva.setId(1L);
        reservaActiva.setFechaInicio(hoy.minusDays(1));
        reservaActiva.setFechaFin(hoy.plusDays(1));
        reservaActiva.setEstado(Estado.RESERVADA);
        reservaActiva.setActivo(true);

        // Reserva que debería pasar a FINALIZADA
        Reserva reservaFinalizada = new Reserva();
        reservaFinalizada.setId(2L);
        reservaFinalizada.setFechaInicio(hoy.minusDays(5));
        reservaFinalizada.setFechaFin(hoy.minusDays(1));
        reservaFinalizada.setEstado(Estado.ACTIVA);
        reservaFinalizada.setActivo(true);

        List<Reserva> reservas = Arrays.asList(reservaActiva, reservaFinalizada);
        when(reservaRepository.findReservasActivas()).thenReturn(reservas);

        reservaScheduler.actualizarEstadosReservas();

        // Verificaciones
        assertEquals(Estado.ACTIVA, reservaActiva.getEstado());
        assertEquals(Estado.FINALIZADA, reservaFinalizada.getEstado());
        assertFalse(reservaFinalizada.getActivo());

        verify(reservaRepository).save(reservaActiva);
        verify(reservaRepository).save(reservaFinalizada);
    }

    @Test
    void actualizarEstadosReservas_sinReservas_noHaceNada() {
        when(reservaRepository.findReservasActivas()).thenReturn(List.of());

        reservaScheduler.actualizarEstadosReservas();

        verify(reservaRepository, never()).save(any());
    }
}


