package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.PagoPendienteMapper;
import org.example.zarp_back.model.dto.pagosPendientes.PagoPendienteResponseDTO;
import org.example.zarp_back.model.entity.PagoPendiente;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.model.enums.EstadoPagosPendientes;
import org.example.zarp_back.repository.PagosPendientesRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.example.zarp_back.service.utils.WebSocketsNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoPendienteService {

    @Autowired
    private PagosPendientesRepository pagosPendientesRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private PagoPendienteMapper pagoPendienteMapper;
    @Autowired
    private WebSocketsNotificacion webSocketsNotificacion;

    public void save(Long reservaID){

        Reserva reserva = reservaRepository.findById(reservaID).orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        Double comision = reserva.getPrecioTotal() * 0.1;
        PagoPendiente pagosPendientes = PagoPendiente.builder()
                .formaPago(reserva.getFormaPago())
                .fechaCreacion(LocalDateTime.now())
                .propietario(reserva.getCliente())
                .monto(reserva.getPrecioTotal()-comision)
                .estadoPagosPendientes(EstadoPagosPendientes.PENDIENTE)
                .build();
        PagoPendiente pagoPendienteSave =  pagosPendientesRepository.save(pagosPendientes);
        webSocketsNotificacion.NotificarSave("pagosPendientes", pagoPendienteMapper.toDto(pagoPendienteSave));
    }

    public PagoPendienteResponseDTO toggleActivo(Long id){

        PagoPendiente pagoPendiente = pagosPendientesRepository.findById(id).orElseThrow(() -> new NotFoundException("Pago pendiente no encontrado"));
        pagoPendiente.setActivo(!pagoPendiente.getActivo());
        pagosPendientesRepository.save(pagoPendiente);
        return pagoPendienteMapper.toDto(pagoPendiente);
    }

    public List<PagoPendienteResponseDTO> findActivos(){

        List<PagoPendiente> pagosPendientes = pagosPendientesRepository.findByActivoTrue();
        return pagoPendienteMapper.toDtoList(pagosPendientes);
    }

    public PagoPendienteResponseDTO cambiarEstado(Long id){
        PagoPendiente pagoPendiente = pagosPendientesRepository.findById(id).orElseThrow(() -> new NotFoundException("Pago pendiente no encontrado"));

        if (!pagoPendiente.getActivo()){
            throw new IllegalStateException("No se puede cambiar el estado de un pago pendiente inactivo");
        }

        pagoPendiente.setEstadoPagosPendientes(pagoPendiente.getEstadoPagosPendientes().siguiente());

        if (pagoPendiente.getEstadoPagosPendientes() == EstadoPagosPendientes.COMPLETADO){
            pagoPendiente.setActivo(false);
        }

        pagosPendientesRepository.save(pagoPendiente);
        webSocketsNotificacion.NotificarUpdate("pagosPendientes", pagoPendienteMapper.toDto(pagoPendiente));
        return pagoPendienteMapper.toDto(pagoPendiente);

    }

    public PagoPendienteResponseDTO getById(Long id){
        PagoPendiente pagoPendiente = pagosPendientesRepository.findById(id).orElseThrow(() -> new NotFoundException("Pago pendiente no encontrado"));
        return pagoPendienteMapper.toDto(pagoPendiente);
    }

    public List<PagoPendienteResponseDTO> getByEstado(EstadoPagosPendientes estado){

        List<PagoPendiente> pagosPendientes = pagosPendientesRepository.findByEstadoPagosPendientes(estado);
        return pagoPendienteMapper.toDtoList(pagosPendientes);

    }

}
