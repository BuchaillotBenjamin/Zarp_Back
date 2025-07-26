package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ImagenMapper;
import org.example.zarp_back.config.mappers.PropiedadMapper;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.entity.*;
import org.example.zarp_back.model.enums.VerificacionPropiedad;
import org.example.zarp_back.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;

@Service
public class PropiedadService extends GenericoServiceImpl<Propiedad, PropiedadDTO, PropiedadResponseDTO, Long> {

    @Autowired
    private PropiedadMapper propiedadMapper;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private TipoPersonaRepository tipoPersonaRepository;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    @Autowired
    private TipoPropiedadRepository tipoPropiedadRepository;
    @Autowired
    private AmbienteRepository ambienteRepository;
    @Autowired
    private ImagenMapper imagenMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PropiedadService.class);




    public PropiedadService(PropiedadRepository propiedadRepository, PropiedadMapper propiedadMapper) {
        super(propiedadRepository, propiedadMapper);
        this.propiedadMapper = propiedadMapper;
    }

    @Override
    @Transactional
    public PropiedadResponseDTO save(PropiedadDTO propiedadDTO) {

        Propiedad propiedad = propiedadMapper.toEntity(propiedadDTO);

        //detalle tipo personas
        propiedad.setDetalleTipoPersonas(new ArrayList<>());

        for (DetalleTipoPersonaDTO detalle: propiedadDTO.getDetalleTipoPersonas()) {

            TipoPersona tipoPersona = tipoPersonaRepository.findById(detalle.getTipoPersonaId())
                    .orElseThrow(() -> new RuntimeException("Tipo de persona no encontrado con ID: " + detalle.getTipoPersonaId()));

            DetalleTipoPersona detalleTipoPersona = DetalleTipoPersona.builder()
                    .cantidad(detalle.getCantidad())
                    .tipoPersona(tipoPersona)
                    .propiedad(propiedad)
                    .build();

            propiedad.getDetalleTipoPersonas().add(detalleTipoPersona);
        }
        //detalle caracteristicas
        propiedad.setDetalleCaracteristicas(new ArrayList<>());

        for (DetalleCaracteristicaDTO detalle : propiedadDTO.getDetalleCaracteristicas()) {

            Caracteristica caracteristica = caracteristicaRepository.findById(detalle.getCaracteristicaId())
                    .orElseThrow(() -> new RuntimeException("Característica no encontrada con ID: " + detalle.getCaracteristicaId()));

            DetalleCaracteristica detalleCaracteristica = DetalleCaracteristica.builder()
                    .propiedad(propiedad)
                    .caracteristica(caracteristica)
                    .build();

            propiedad.getDetalleCaracteristicas().add(detalleCaracteristica);

        }
        //detalle imagenes
        propiedad.setDetalleImagenes(new ArrayList<>());

        for(DetalleImagenPropiedadDTO detalle : propiedadDTO.getDetalleImagenes()) {

            Imagen imagen = imagenMapper.toEntity(detalle.getImagen());

            DetalleImagenPropiedad detalleImagenPropiedad = DetalleImagenPropiedad.builder()
                    .propiedad(propiedad)
                    .imgPrincipal(detalle.getImgPrincipal())
                    .imagen(imagen)
                    .build();

            propiedad.getDetalleImagenes().add(detalleImagenPropiedad);

        }
        //detalle ambientes
        propiedad.setDetalleAmbientes(new ArrayList<>());
        for (DetalleAmbienteDTO detalle: propiedadDTO.getDetalleAmbientes()){

            Ambiente ambiente = ambienteRepository.findById(detalle.getAmbienteId())
                    .orElseThrow(() -> new RuntimeException("Ambiente no encontrado con ID: " + detalle.getAmbienteId()));

            DetalleAmbiente detalleAmbiente = DetalleAmbiente.builder()
                    .propiedad(propiedad)
                    .ambiente(ambiente)
                    .cantidad(detalle.getCantidad())
                    .build();

            propiedad.getDetalleAmbientes().add(detalleAmbiente);

        }
        //tipo Propiedad
        TipoPropiedad tipoPropiedad = tipoPropiedadRepository.findById(propiedadDTO.getTipoPropiedadId())
                .orElseThrow(() -> new RuntimeException("Tipo de propiedad no encontrado con ID: " + propiedadDTO.getTipoPropiedadId()));
        propiedad.setTipoPropiedad(tipoPropiedad);

        //Verificacion pendiente
        propiedad.setVerificacionPropiedad(VerificacionPropiedad.PENDIENTE);


        propiedadRepository.save(propiedad);
        return propiedadMapper.toResponseDTO(propiedad);

    }


    // Aquí puedes agregar métodos específicos para el servicio de Propiedad si es necesario
}
