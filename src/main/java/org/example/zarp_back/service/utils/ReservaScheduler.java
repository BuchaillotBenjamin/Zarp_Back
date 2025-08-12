package org.example.zarp_back.service.utils;

import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReservaScheduler {

    @Autowired
    private ReservaRepository reservaRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Argentina/Buenos_Aires")
    public void actualizarEstadosReservas() {

        List<Reserva> reservas = reservaRepository.findReservasActivas();

        LocalDate now = LocalDate.now();

        for(Reserva reserva : reservas) {
            boolean update = false;
            if ((now.isEqual(reserva.getFechaInicio()) || now.isAfter(reserva.getFechaInicio()))
                    && (now.isBefore(reserva.getFechaFin()) || now.isEqual(reserva.getFechaFin()))) {
                // La reserva está en periodo activo
                reserva.setEstado(Estado.ACTIVA);
                update = true;
            }  else if (now.isAfter(reserva.getFechaFin())) {
                reserva.setEstado(Estado.FINALIZADA);
                reserva.setActivo(false);
                update = true;
            }
            if(update){
                try{
                    reservaRepository.save(reserva);
                }catch (Exception exception){
                    System.out.println("Error al actualizar la reserva con id: " + reserva.getId());
                }
            }

        }

    }


}
