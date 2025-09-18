package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ReservaMapper;
import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaFechaDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.model.enums.VerificacionPropiedad;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new NotFoundException("Propiedad con el id " + reservaDTO.getPropiedadId() + " no encontrada"));
        reserva.setPropiedad(propiedad);
        if (propiedad.getVerificacionPropiedad()!= VerificacionPropiedad.APROBADA){
            throw new RuntimeException("La propiedad con el id " + reservaDTO.getPropiedadId() + " no está aprobada para reservas");
        }

        //cliente
        Cliente cliente = clienteRepository.findById(reservaDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + reservaDTO.getClienteId() + " no encontrado"));
        reserva.setCliente(cliente);
        if (!cliente.getRol().equals(Rol.PROPIETARIO)) {
            throw new RuntimeException("El cliente con el id " + reservaDTO.getClienteId() + " no tiene las verificaciones necesarias");
        }
        if (propiedad.getPropietario().getId().equals(cliente.getId())) {
            throw new RuntimeException("El cliente con el id " + reservaDTO.getClienteId() + " no puede reservar su propia propiedad");
        }

        //estado
        reserva.setEstado(Estado.PENDIENTE);

        reservaRepository.save(reserva);

        return reservaMapper.toResponseDTO(reserva);
    }

    public ReservaResponseDTO reservaFinalizada(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva con el id " + id + " no encontrada"));
        reserva.setEstado(Estado.FINALIZADA);
        reservaRepository.save(reserva);
        return reservaMapper.toResponseDTO(reserva);
    }

    public List<ReservaFechaDTO> fechasReservadas(Long propiedadId){

        if (!propiedadRepository.existsById(propiedadId)){
            throw new NotFoundException("La propiedad con el id " + propiedadId+ " no existe");
        }

        List<Reserva> reservas = reservaRepository.findReservasActivasPorPropiedad(propiedadId);

        return reservaMapper.toFechaDTOList(reservas);
    }

    public Void cambiarEstado(Long id, Estado estado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva con el id " + id + " no encontrada"));
        reserva.setEstado(estado);
        reservaRepository.save(reserva);
        return null;
    }

    // Aquí puedes agregar métodos específicos para el servicio de Reserva si es necesario
}
