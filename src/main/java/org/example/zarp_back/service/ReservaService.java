package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ReservaMapper;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.repository.ReservaRepository;

public class ReservaService extends GenericoServiceImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long> {

    public ReservaService(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        super(reservaRepository, reservaMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Reserva si es necesario
}
