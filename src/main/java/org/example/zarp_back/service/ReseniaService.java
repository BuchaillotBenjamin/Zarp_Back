package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ReseniaMapper;
import org.example.zarp_back.model.dto.resenia.ReseniaDTO;
import org.example.zarp_back.model.dto.resenia.ReseniaResponseDTO;
import org.example.zarp_back.model.entity.Propiedad;
import org.example.zarp_back.model.entity.Resenia;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.PropiedadRepository;
import org.example.zarp_back.repository.ReseniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReseniaService extends GenericoServiceImpl<Resenia, ReseniaDTO, ReseniaResponseDTO, Long> {

    @Autowired
    private ReseniaMapper reseniaMapper;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public ReseniaService(ReseniaRepository reseniaRepository, ReseniaMapper reseniaMapper) {
        super(reseniaRepository, reseniaMapper);
    }

    @Override
    @Transactional
    public ReseniaResponseDTO save(ReseniaDTO reseniaDTO) {

        Resenia resenia = reseniaMapper.toEntity(reseniaDTO);
        Propiedad propiedad = propiedadRepository.findById(reseniaDTO.getPropiedadId())
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada con id: " + reseniaDTO.getPropiedadId()));


        //TODO:verificar que tenga un reserva finalizada en la propiedad
        //metodobooleano de reservaRepo



        //propiedad
        resenia.setPropiedad(propiedad);


        //cliente
        resenia.setUsuario(clienteRepository.findById(reseniaDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + reseniaDTO.getUsuarioId())));

        propiedad.getResenias().add(resenia);
        propiedadRepository.save(propiedad);

        return reseniaMapper.toResponseDTO(resenia);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Resenia si es necesario
}
