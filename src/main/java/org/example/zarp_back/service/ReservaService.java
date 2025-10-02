package org.example.zarp_back.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ReservaMapper;
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
import org.example.zarp_back.service.utils.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservaService extends GenericoServiceImpl<Reserva, ReservaDTO, ReservaResponseDTO, Long> {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReservaMapper reservaMapper;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private NotificacionService notificacionService;


    public ReservaService(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        super(reservaRepository, reservaMapper);
    }

    @Override
    @Transactional
    public ReservaResponseDTO save(ReservaDTO reservaDTO) {

        Reserva reserva = reservaMapper.toEntity(reservaDTO);

        if(!esReservaValida(reservaDTO)){
            log.error("La reserva no es válida por incumplimiento de alguna regla de negocio");
            throw new IllegalArgumentException("La reserva no es válida por incumplimiento de alguna regla de negocio");
        }

        //propiedad
        Propiedad propiedad = propiedadRepository.findById(reservaDTO.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad con el id " + reservaDTO.getPropiedadId() + " no encontrada"));
        reserva.setPropiedad(propiedad);

        //cliente
        Cliente cliente = clienteRepository.findById(reservaDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + reservaDTO.getClienteId() + " no encontrado"));
        reserva.setCliente(cliente);

        //estado
        reserva.setEstado(Estado.PENDIENTE);

        reservaRepository.save(reserva);

        log.info("Reserva con el id {} creada exitosamente", reserva.getId());
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
        if(estado==Estado.RESERVADA){

            try {
                notificacionService.notificarReservaPropietario(reserva);
                notificacionService.notificarReservaCliente(reserva);
            } catch (MessagingException e) {
                log.error("Error al enviar notificaciones para la reserva con id {}: {}", id, e.getMessage());
            }

        }

        reservaRepository.save(reserva);
        return null;
    }

    public List<ReservaResponseDTO>obtenerReservasPorClienteId(Long clienteId){

        List<Reserva> reservas = reservaRepository.findByClienteId(clienteId);
        return reservaMapper.toResponseDTOList(reservas);
    }

    public List<ReservaResponseDTO> obtenerReservasPorPropiedad(Long propiedadId){
        List<Reserva> reservas = reservaRepository.findByPropiedadId(propiedadId);
        return reservaMapper.toResponseDTOList(reservas);
    }

    public List<List<ReservaResponseDTO>> obeterReservasDePropietario(Long propietarioId){

        Cliente propietario = clienteRepository.findById(propietarioId)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + propietarioId + " no encontrado"));

        List<Propiedad> propiedades = propiedadRepository.findByPropietario_Id(propietarioId);

        List<List<ReservaResponseDTO>> reservasDePropiedades = propiedades.stream()
                .map(propiedad -> reservaMapper.toResponseDTOList(reservaRepository.findByPropiedadId(propiedad.getId())))
                .toList();

        return reservasDePropiedades;
    }

    public boolean esReservaValida(ReservaDTO reservaDTO) {
        // Verificar solapamiento de fechas
        List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(
                reservaDTO.getPropiedadId(),
                reservaDTO.getFechaInicio(),
                reservaDTO.getFechaFin()
        );
        if (!reservasSolapadas.isEmpty()) {
            log.warn("Reserva inválida: solapamiento con otras reservas para propiedad {}", reservaDTO.getPropiedadId());
            return false;
        }

        // Verificar propiedad
        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(reservaDTO.getPropiedadId());
        if (propiedadOpt.isEmpty()) {
            log.warn("Reserva inválida: propiedad {} no encontrada", reservaDTO.getPropiedadId());
            return false;
        }

        Propiedad propiedad = propiedadOpt.get();
        if (propiedad.getVerificacionPropiedad() != VerificacionPropiedad.APROBADA) {
            log.warn("Reserva inválida: propiedad {} no está aprobada", reservaDTO.getPropiedadId());
            return false;
        }

        // Verificar cliente
        Optional<Cliente> clienteOpt = clienteRepository.findById(reservaDTO.getClienteId());
        if (clienteOpt.isEmpty()) {
            log.warn("Reserva inválida: cliente {} no encontrado", reservaDTO.getClienteId());
            return false;
        }

        Cliente cliente = clienteOpt.get();
        if (!cliente.getRol().equals(Rol.PROPIETARIO)) {
            log.warn("Reserva inválida: cliente {} no tiene rol PROPIETARIO", reservaDTO.getClienteId());
            return false;
        }

        if (propiedad.getPropietario().getId().equals(cliente.getId())) {
            log.warn("Reserva inválida: cliente {} intenta reservar su propia propiedad", reservaDTO.getClienteId());
            return false;
        }

        return true;
    }


    // Aquí puedes agregar métodos específicos para el servicio de Reserva si es necesario
}
