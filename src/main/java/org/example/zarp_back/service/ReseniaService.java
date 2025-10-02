package org.example.zarp_back.service;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.PropiedadMapper;
import org.example.zarp_back.config.mappers.ReseniaMapper;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaResponseDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.model.enums.Estado;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReseniaRepository;
import org.example.zarp_back.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ReseniaService extends GenericoServiceImpl<Resenia, ReseniaDTO, ReseniaResponseDTO, Long> {

    @Autowired
    private ReseniaMapper reseniaMapper;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReseniaRepository reseniaRepository;
    @Autowired
    private PropiedadMapper propiedadMapper;


    public ReseniaService(ReseniaRepository reseniaRepository, ReseniaMapper reseniaMapper) {
        super(reseniaRepository, reseniaMapper);
    }

    @Override
    @Transactional
    public ReseniaResponseDTO save(ReseniaDTO reseniaDTO) {

        if (reseniaRepository.existsByPropiedadIdAndUsuarioId(reseniaDTO.getPropiedadId(),reseniaDTO.getUsuarioId())) {
            log.error("El usuario ya ha dejado una reseña en esta propiedad.");
            throw new RuntimeException("El usuario ya ha dejado una reseña en esta propiedad.");
        }

        boolean tieneFinalizada = reservaRepository.existsByClienteIdAndPropiedadIdAndEstado(reseniaDTO.getUsuarioId(), reseniaDTO.getPropiedadId(), Estado.FINALIZADA);
        boolean tieneActiva = reservaRepository.existsByClienteIdAndPropiedadIdAndEstado(reseniaDTO.getUsuarioId(), reseniaDTO.getPropiedadId(), Estado.ACTIVA);

        if (!(tieneFinalizada || tieneActiva)) {
            log.error("El usuario no tiene una reserva finalizada o activa en esta propiedad.");
            throw new RuntimeException("El usuario no tiene una reserva finalizada o activa en esta propiedad.");
        }

        Resenia resenia = reseniaMapper.toEntity(reseniaDTO);

        //propiedad
        Propiedad propiedad = propiedadRepository.findById(reseniaDTO.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada con id: " + reseniaDTO.getPropiedadId()));

        resenia.setPropiedad(propiedad);


        //cliente
        resenia.setUsuario(clienteRepository.findById(reseniaDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + reseniaDTO.getUsuarioId())));

        propiedad.getResenias().add(resenia);
        propiedadRepository.save(propiedad);
        log.info("Reseña guardada correctamente: {}", resenia);

        if (reseniaRepository.existsByPropiedadIdAndUsuarioId(reseniaDTO.getPropiedadId(),reseniaDTO.getUsuarioId())) {
            Resenia reseniaGuardada = reseniaRepository.findByPropiedadIdAndUsuarioId(resenia.getPropiedad().getId(), resenia.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Error al guardar la reseña."));
            return reseniaMapper.toResponseDTO(reseniaGuardada);
        }else {
            throw new RuntimeException("Error al guardar la reseña.");
        }


    }


    // Aquí puedes agregar métodos específicos para el servicio de Resenia si es necesario
}
