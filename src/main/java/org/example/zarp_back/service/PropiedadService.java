package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.DireccionMapper;
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
        agregarDetalleTipoPersonas(propiedad, propiedadDTO);

        //detalle caracteristicas
        propiedad.setDetalleCaracteristicas(new ArrayList<>());
        agregarDetalleCaracteristicas(propiedad, propiedadDTO);

        //detalle imagenes
        propiedad.setDetalleImagenes(new ArrayList<>());
        agregarDetalleImagenes(propiedad, propiedadDTO);

        //detalle ambientes
        propiedad.setDetalleAmbientes(new ArrayList<>());
        agregarDetalleAmbientes(propiedad, propiedadDTO);

        //tipo Propiedad
        propiedad.setTipoPropiedad(new TipoPropiedad());
        asignarTipoPropiedad(propiedad, propiedadDTO);

        //Verificacion pendiente
        propiedad.setVerificacionPropiedad(VerificacionPropiedad.PENDIENTE);

        //vincular cliente


        propiedadRepository.save(propiedad);
        return propiedadMapper.toResponseDTO(propiedad);

    }

    @Override
    @Transactional
    public PropiedadResponseDTO update(Long id, PropiedadDTO propiedadDTO) {
    Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada con ID: " + id));

        if (!propiedad.getNombre().equals(propiedadDTO.getNombre())) {
            propiedad.setNombre(propiedadDTO.getNombre());
        }
        if (!propiedad.getDescripcion().equals(propiedadDTO.getDescripcion())) {
            propiedad.setDescripcion(propiedadDTO.getDescripcion());
        }
        if (!propiedad.getPrecioPorNoche().equals(propiedadDTO.getPrecioPorNoche())) {
            propiedad.setPrecioPorNoche(propiedadDTO.getPrecioPorNoche());
        }

        //detalle tipo personas
        agregarDetalleTipoPersonas(propiedad, propiedadDTO);

        //detalle caracteristicas
        agregarDetalleCaracteristicas(propiedad, propiedadDTO);

        //detalle imagenes
        agregarDetalleImagenes(propiedad, propiedadDTO);

        //detalle ambientes
        agregarDetalleAmbientes(propiedad, propiedadDTO);

        //tipo Propiedad
        asignarTipoPropiedad(propiedad, propiedadDTO);

        //reseñas
        propiedad.setResenias(new ArrayList<>());

        propiedadRepository.save(propiedad);

        return propiedadMapper.toResponseDTO(propiedad);
    }



    private void agregarDetalleTipoPersonas(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        propiedad.getDetalleTipoPersonas().clear();
        for (DetalleTipoPersonaDTO detalle : propiedadDTO.getDetalleTipoPersonas()) {
            TipoPersona tipoPersona = tipoPersonaRepository.findById(detalle.getTipoPersonaId())
                    .orElseThrow(() -> new RuntimeException("Tipo de persona no encontrado con ID: " + detalle.getTipoPersonaId()));
            DetalleTipoPersona detalleTipoPersona = DetalleTipoPersona.builder()
                    .cantidad(detalle.getCantidad())
                    .tipoPersona(tipoPersona)
                    .propiedad(propiedad)
                    .build();
            propiedad.getDetalleTipoPersonas().add(detalleTipoPersona);
        }
    }

    private void agregarDetalleCaracteristicas(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        propiedad.getDetalleCaracteristicas().clear();
        for (DetalleCaracteristicaDTO detalle : propiedadDTO.getDetalleCaracteristicas()) {
            Caracteristica caracteristica = caracteristicaRepository.findById(detalle.getCaracteristicaId())
                    .orElseThrow(() -> new RuntimeException("Característica no encontrada con ID: " + detalle.getCaracteristicaId()));
            DetalleCaracteristica detalleCaracteristica = DetalleCaracteristica.builder()
                    .propiedad(propiedad)
                    .caracteristica(caracteristica)
                    .build();
            propiedad.getDetalleCaracteristicas().add(detalleCaracteristica);
        }
    }

    private void agregarDetalleImagenes(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        propiedad.getDetalleImagenes().clear();
        for (DetalleImagenPropiedadDTO detalle : propiedadDTO.getDetalleImagenes()) {
            Imagen imagen = imagenMapper.toEntity(detalle.getImagen());
            DetalleImagenPropiedad detalleImagenPropiedad = DetalleImagenPropiedad.builder()
                    .propiedad(propiedad)
                    .imgPrincipal(detalle.getImgPrincipal())
                    .imagen(imagen)
                    .build();
            propiedad.getDetalleImagenes().add(detalleImagenPropiedad);
        }
    }

    private void agregarDetalleAmbientes(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        propiedad.getDetalleAmbientes().clear();
        for (DetalleAmbienteDTO detalle : propiedadDTO.getDetalleAmbientes()) {
            Ambiente ambiente = ambienteRepository.findById(detalle.getAmbienteId())
                    .orElseThrow(() -> new RuntimeException("Ambiente no encontrado con ID: " + detalle.getAmbienteId()));
            DetalleAmbiente detalleAmbiente = DetalleAmbiente.builder()
                    .propiedad(propiedad)
                    .ambiente(ambiente)
                    .cantidad(detalle.getCantidad())
                    .build();
            propiedad.getDetalleAmbientes().add(detalleAmbiente);
        }
    }

    private void asignarTipoPropiedad(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        TipoPropiedad tipoPropiedad = tipoPropiedadRepository.findById(propiedadDTO.getTipoPropiedadId())
                .orElseThrow(() -> new RuntimeException("Tipo de propiedad no encontrado con ID: " + propiedadDTO.getTipoPropiedadId()));
        propiedad.setTipoPropiedad(tipoPropiedad);
    }



}



