package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ReseniaMapper;
import org.example.zarp_back.model.dto.reseña.ReseniaDTO;
import org.example.zarp_back.model.dto.reseña.ReseniaResponseDTO;
import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.repository.ReseniaRepository;

public class ReseniaService extends GenericoServiceImpl<Resenia, ReseniaDTO, ReseniaResponseDTO, Long> {

    public ReseniaService(ReseniaRepository reseniaRepository, ReseniaMapper reseniaMapper) {
        super(reseniaRepository, reseniaMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Resenia si es necesario
}
