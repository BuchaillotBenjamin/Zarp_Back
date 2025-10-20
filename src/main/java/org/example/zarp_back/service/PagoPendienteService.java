package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.PagoPendienteMapper;
import org.example.zarp_back.model.dto.pagosPendientes.PagoPendienteResponseDTO;
import org.example.zarp_back.model.entity.PagoPendiente;
import org.example.zarp_back.model.entity.Reserva;
import org.example.zarp_back.repository.PagosPendientesRepository;
import org.example.zarp_back.repository.ReservaRepository;
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

    public void save(Long reservaID){

        Reserva reserva = reservaRepository.findById(reservaID).orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        Double comision = reserva.getPrecioTotal() * 0.1;
        PagoPendiente pagosPendientes = PagoPendiente.builder()
                .formaPago(reserva.getFormaPago())
                .fechaCreacion(LocalDateTime.now())
                .propietario(reserva.getCliente())
                .monto(reserva.getPrecioTotal()-comision)
                .build();
        pagosPendientesRepository.save(pagosPendientes);
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

}
