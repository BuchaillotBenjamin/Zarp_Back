package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.DireccionMapper;
import org.example.zarp_back.config.mappers.ImagenMapper;
import org.example.zarp_back.config.mappers.PropiedadMapper;
import org.example.zarp_back.model.dto.detalleAmbiente.DetalleAmbienteDTO;
import org.example.zarp_back.model.dto.detalleCaracteristica.DetalleCaracteristicaDTO;
import org.example.zarp_back.model.dto.detalleImagenPropiedad.DetalleImagenPropiedadDTO;
import org.example.zarp_back.model.dto.detalleTipoPersona.DetalleTipoPersonaDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadDTO;
import org.example.zarp_back.model.dto.propiedad.PropiedadResponseDTO;
import org.example.zarp_back.model.dto.reserva.ReservaResponseDTO;
import org.example.zarp_back.model.dto.tipoPropiedad.TipoPropiedadDTO;
import org.example.zarp_back.model.entity.*;
import org.example.zarp_back.model.enums.Provincia;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.model.enums.VerificacionPropiedad;
import org.example.zarp_back.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    ClienteRepository clienteRepository;
    
    public PropiedadService(PropiedadRepository propiedadRepository, PropiedadMapper propiedadMapper) {
        super(propiedadRepository, propiedadMapper);
        this.propiedadMapper = propiedadMapper;
    }

    @Override
    @Transactional
    public PropiedadResponseDTO save(PropiedadDTO propiedadDTO) {

        Propiedad propiedad = propiedadMapper.toEntity(propiedadDTO);

        //vincular cliente y verificar su rol
        Cliente propietario = clienteRepository.findById(propiedadDTO.getPropietarioId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + propiedadDTO.getPropietarioId()));
        if (propietario.getRol()!= Rol.PROPIETARIO){
            throw new RuntimeException("El cliente no esta verificado como propietario");
        }
        propiedad.setPropietario(propietario);

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



        propiedadRepository.save(propiedad);
        return propiedadMapper.toResponseDTO(propiedad);

    }

    @Override
    @Transactional
    public PropiedadResponseDTO update(Long id, PropiedadDTO propiedadDTO) {
    Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Propiedad no encontrada con ID: " + id));

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
        if (!sonIgualesPorIdTipoPersona(propiedad.getDetalleTipoPersonas(), propiedadDTO.getDetalleTipoPersonas())) {
            agregarDetalleTipoPersonas(propiedad, propiedadDTO);
        }

        //detalle caracteristicas
        if (!sonIgualesPorIdDetalleCaracteristica(propiedad.getDetalleCaracteristicas(), propiedadDTO.getDetalleCaracteristicas())) {
            agregarDetalleCaracteristicas(propiedad, propiedadDTO);
        }

        //detalle imagenes
        if (!sonIgualesPorUrlDetalleImagen(propiedad.getDetalleImagenes(), propiedadDTO.getDetalleImagenes())) {
            agregarDetalleImagenes(propiedad, propiedadDTO);
        }

        //detalle ambientes
        if (!sonIgualesPorIdDetalleAmbiente(propiedad.getDetalleAmbientes(), propiedadDTO.getDetalleAmbientes())) {
            agregarDetalleAmbientes(propiedad, propiedadDTO);
        }

        //tipo Propiedad
        if (!propiedadDTO.getTipoPropiedadId().equals(propiedad.getTipoPropiedad().getId())) {
            asignarTipoPropiedad(propiedad, propiedadDTO);
        }

        //reseñas
        propiedad.setResenias(new ArrayList<>());

        propiedadRepository.save(propiedad);

        return propiedadMapper.toResponseDTO(propiedad);
    }

    public List<List<PropiedadResponseDTO>> getPropiedadesByProvincia(){

        List<List<PropiedadResponseDTO>> listasProvincias = new ArrayList<>();

        for (Provincia provincia : Provincia.values()) {
            List<Propiedad> propiedades = propiedadRepository.findByDireccionProvinciaAndActivoAndVerificacionPropiedad(
                    provincia,
                    true,
                    VerificacionPropiedad.APROBADA);
            if (!propiedades.isEmpty()) {
                listasProvincias.add(propiedadMapper.toResponseDTOList(propiedades));
            }
        }

        return listasProvincias;
    }

    public List<PropiedadResponseDTO> getPropiedadesByProvincia(Provincia provincia) {

        List<Propiedad> propiedades = propiedadRepository.findByDireccionProvinciaAndActivoAndVerificacionPropiedad(
                provincia,
                true,
                VerificacionPropiedad.APROBADA);
        return propiedadMapper.toResponseDTOList(propiedades);
    }

    public List<PropiedadResponseDTO> getPropiedadesByCliente(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + idCliente));
        List<Propiedad> propiedades = propiedadRepository.findByPropietario_Id(idCliente);
        return propiedadMapper.toResponseDTOList(propiedades);
    }

    public List<PropiedadResponseDTO> propiedadesVerificar() {

        List<Propiedad> propiedades = propiedadRepository.findByVerificacionPropiedad(VerificacionPropiedad.PENDIENTE);
        return propiedadMapper.toResponseDTOList(propiedades);

    }

    public PropiedadResponseDTO verificarPropiedad(Long id, boolean aprobado) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Propiedad no encontrada con ID: " + id));
        if (aprobado) {
            propiedad.setVerificacionPropiedad(VerificacionPropiedad.APROBADA);
        } else {
            propiedad.setVerificacionPropiedad(VerificacionPropiedad.RECHAZADA);
        }
        propiedadRepository.save(propiedad);
        return propiedadMapper.toResponseDTO(propiedad);
    }


    //metodos privadps

    private void agregarDetalleTipoPersonas(Propiedad propiedad, PropiedadDTO propiedadDTO) {
        propiedad.getDetalleTipoPersonas().clear();
        for (DetalleTipoPersonaDTO detalle : propiedadDTO.getDetalleTipoPersonas()) {
            TipoPersona tipoPersona = tipoPersonaRepository.findById(detalle.getTipoPersonaId())
                    .orElseThrow(() -> new NotFoundException("Tipo de persona no encontrado con ID: " + detalle.getTipoPersonaId()));
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
                    .orElseThrow(() -> new NotFoundException("Característica no encontrada con ID: " + detalle.getCaracteristicaId()));
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
                    .orElseThrow(() -> new NotFoundException("Ambiente no encontrado con ID: " + detalle.getAmbienteId()));
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
                .orElseThrow(() -> new NotFoundException("Tipo de propiedad no encontrado con ID: " + propiedadDTO.getTipoPropiedadId()));
        propiedad.setTipoPropiedad(tipoPropiedad);
    }

    private boolean sonIgualesPorIdTipoPersona(List<DetalleTipoPersona> listaEntidad, List<DetalleTipoPersonaDTO> listaDto) {
        if (listaEntidad.size() != listaDto.size()) return false;

        Set<Long> idsEntidad = listaEntidad.stream()
                .map(detalle -> detalle.getTipoPersona().getId())
                .collect(Collectors.toSet());

        Set<Long> idsDto = listaDto.stream()
                .map(DetalleTipoPersonaDTO::getTipoPersonaId)
                .collect(Collectors.toSet());

        return idsEntidad.equals(idsDto);
    }

    private boolean sonIgualesPorIdDetalleCaracteristica(List<DetalleCaracteristica> listaEntidad, List<DetalleCaracteristicaDTO> listaDto) {
        if (listaEntidad.size() != listaDto.size()) return false;

        Set<Long> idsEntidad = listaEntidad.stream()
                .map(detalle -> detalle.getCaracteristica().getId())
                .collect(Collectors.toSet());

        Set<Long> idsDto = listaDto.stream()
                .map(DetalleCaracteristicaDTO::getCaracteristicaId)
                .collect(Collectors.toSet());

        return idsEntidad.equals(idsDto);
    }

    private boolean sonIgualesPorUrlDetalleImagen(List<DetalleImagenPropiedad> listaEntidad, List<DetalleImagenPropiedadDTO> listaDto) {
        if (listaEntidad.size() != listaDto.size()) return false;

        Set<String> urlsEntidad = listaEntidad.stream()
                .map(detalle -> detalle.getImagen().getUrlImagen())
                .collect(Collectors.toSet());

        Set<String> urlsDto = listaDto.stream()
                .map(detalle -> detalle.getImagen().getUrlImagen())
                .collect(Collectors.toSet());

        return urlsEntidad.equals(urlsDto);
    }

    private boolean sonIgualesPorIdDetalleAmbiente(List<DetalleAmbiente> listaEntidad, List<DetalleAmbienteDTO> listaDto) {
        if (listaEntidad.size() != listaDto.size()) return false;

        Set<Long> idsEntidad = listaEntidad.stream()
                .map(detalle -> detalle.getAmbiente().getId())
                .collect(Collectors.toSet());

        Set<Long> idsDto = listaDto.stream()
                .map(DetalleAmbienteDTO::getAmbienteId)
                .collect(Collectors.toSet());

        return idsEntidad.equals(idsDto);
    }

}



