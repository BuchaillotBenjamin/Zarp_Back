package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ReservaMapper;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService extends GenericoServiceImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long> {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReservaMapper reservaMapper;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private ClienteRepository clienteRepository;


    public ReservaService(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        super(reservaRepository, reservaMapper);
    }

    @Override
    @Transactional
    public ReservaResponseDTO save(ReservaDTO reservaDTO) {

        Reserva reserva = reservaMapper.toEntity(reservaDTO);

        //TODO:validar que la propiedad no tenga reservas en las fechas indicadas

        //propiedad
        Propiedad propiedad = propiedadRepository.findById(reservaDTO.getPropiedadId())
                .orElseThrow(() -> new RuntimeException("Propiedad con el id " + reservaDTO.getPropiedadId() + " no encontrada"));
        reserva.setPropiedad(propiedad);

        //cliente
        Cliente cliente = clienteRepository.findById(reservaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente con el id " + reservaDTO.getClienteId() + " no encontrado"));
        reserva.setCliente(cliente);

        //estado
        reserva.setEstado(Estado.PENDIENTE);

        reservaRepository.save(reserva);

        return reservaMapper.toResponseDTO(reserva);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Reserva si es necesario
}
