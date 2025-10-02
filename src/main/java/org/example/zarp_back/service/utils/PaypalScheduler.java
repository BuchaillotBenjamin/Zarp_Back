package org.example.zarp_back.service.utils;

import org.example.zarp_back.model.entity.ReservaPayoutPendiente;
import org.example.zarp_back.repository.ReservaPayoutPendienteRepository;
import org.example.zarp_back.service.PaypalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaypalScheduler {

    private static final Logger log = LoggerFactory.getLogger(PaypalScheduler.class);
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private ReservaPayoutPendienteRepository reservaPayoutPendienteRepository;

    @Scheduled(fixedRate = 3 * 60 * 60 * 1000, zone = "America/Argentina/Buenos_Aires") // Cada 3 horas
    //@Scheduled(cron = "0 */5 * * * *") 
    public void procesarPayoutsPendientes() {
        List<ReservaPayoutPendiente> reservasPendientes =  reservaPayoutPendienteRepository.findByActivoTrue();
        log.info("Procesando {} payouts pendientes", reservasPendientes.size());

        for (ReservaPayoutPendiente reservaPendiente : reservasPendientes) {
            log.info("Procesando payout para reserva ID: {}", reservaPendiente.getReservaId());
            paypalService.createPayout(reservaPendiente.getReservaId());
        }
    }


}
