package org.example.zarp_back.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class MercadoPagoScheduler {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    MercadoPagoService mercadoPagoService;

    // Ejecutar todos los días a las 2 AM
    @Scheduled(cron = "0 0 2 * * ?", zone = "America/Argentina/Buenos_Aires")
    public void refrescarTokensPorVencer() {
        LocalDateTime limite = LocalDateTime.now().plusDays(4);

        List<Cliente> clientes = clienteRepository.findByCredencialesMP_TokenExpirationBefore(limite);

        log.info("Refrescando tokens de {} clientes", clientes.size());
        clientes.forEach(mercadoPagoService::refrescarToken);
    }


}
