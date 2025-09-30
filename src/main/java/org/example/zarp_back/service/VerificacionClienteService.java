package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.config.mappers.VerificacionClienteMapper;
import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.VerificacionClienteRepository;
import org.example.zarp_back.service.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerificacionClienteService extends GenericoServiceImpl<VerificacionCliente, VerificacionClienteDTO, VerificacionClienteResponseDTO, Long> {

    @Autowired
    private VerificacionClienteRepository verificacionClienteRepository;
    @Autowired
    private VerificacionClienteMapper verificacionClienteMapper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CryptoUtils cryptoUtils;
    @Autowired
    private ClienteMapper clienteMapper;


    public VerificacionClienteService(VerificacionClienteRepository verificacionClienteRepository, VerificacionClienteMapper verificacionClienteMapper) {
        super(verificacionClienteRepository, verificacionClienteMapper);
    }

    @Override
    @Transactional
    public VerificacionClienteResponseDTO save(VerificacionClienteDTO verificacionClienteDTO) {

        Cliente cliente = clienteRepository.findById(verificacionClienteDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + verificacionClienteDTO.getClienteId()));

        if(verificacionClienteRepository.existsVerificacionActivaByClienteId(verificacionClienteDTO.getClienteId())) {
            throw new RuntimeException("El cliente ya tiene una verificación activa.");
        }else if(cliente.getDocumentoVerificado()){
            throw new RuntimeException("El cliente ya esta verificado.");
        }

        VerificacionCliente verificacionCliente = new VerificacionCliente();

        Imagen fotoFrontal =new Imagen();
        Imagen fotoDocumentoFrontal =new Imagen();
        Imagen fotoDocumentoTrasero =new Imagen();
        try {
            fotoFrontal.setUrlImagen(cryptoUtils.encryptUrl(verificacionClienteDTO.getFotoFrontal().getUrlImagen()));
            fotoDocumentoFrontal.setUrlImagen(cryptoUtils.encryptUrl(verificacionClienteDTO.getFotoDocumentoFrontal().getUrlImagen()));
            fotoDocumentoTrasero.setUrlImagen(cryptoUtils.encryptUrl(verificacionClienteDTO.getFotoDocumentoTrasero().getUrlImagen()));

        }catch (Exception e) {
            throw new RuntimeException("Error al encriptar las imagenes de la verificación.");
        }

        verificacionCliente.setActivo(true);
        //seteo imagenes
        verificacionCliente.setFotoFrontal(fotoFrontal);
        verificacionCliente.setFotoDocumentoFrontal(fotoDocumentoFrontal);
        verificacionCliente.setFotoDocumentoTrasero(fotoDocumentoTrasero);

        //cliente
        verificacionCliente.setCliente(cliente);

        verificacionClienteRepository.save(verificacionCliente);

        return verificacionClienteMapper.toResponseDTO(verificacionCliente);
    }

    public List<VerificacionClienteResponseDTO> getVerificacionesActivas() {
        List<VerificacionCliente> verificaciones = verificacionClienteRepository.findByActivo(true);
        List<VerificacionClienteResponseDTO> responseDTOs = new ArrayList<>();
        for(VerificacionCliente verificacion : verificaciones) {
            VerificacionClienteResponseDTO verificacionResponse = toResponseDTODesencriptado(verificacion);
            responseDTOs.add(verificacionResponse);
        }
        return responseDTOs;
    }

    private VerificacionClienteResponseDTO toResponseDTODesencriptado(VerificacionCliente verificacionCliente) {
        VerificacionClienteResponseDTO responseDTO = new VerificacionClienteResponseDTO();

        responseDTO.setId(verificacionCliente.getId());
        responseDTO.setActivo(verificacionCliente.getActivo());
        responseDTO.setCliente(clienteMapper.toResponseDTO(verificacionCliente.getCliente()));
        Imagen fotoFrontal = new Imagen();
        Imagen fotoDocumentoFrontal = new Imagen();
        Imagen fotoDocumentoTrasero = new Imagen();

        try {
            fotoFrontal.setUrlImagen(cryptoUtils.decryptUrl(verificacionCliente.getFotoFrontal().getUrlImagen()));

            fotoDocumentoFrontal.setUrlImagen(cryptoUtils.decryptUrl(verificacionCliente.getFotoDocumentoFrontal().getUrlImagen()));

            fotoDocumentoTrasero.setUrlImagen(cryptoUtils.decryptUrl(verificacionCliente.getFotoDocumentoTrasero().getUrlImagen()));

        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar las imágenes de la verificación." + e.getMessage());
        }
        responseDTO.setFotoFrontal(fotoFrontal);
        responseDTO.setFotoDocumentoFrontal(fotoDocumentoFrontal);
        responseDTO.setFotoDocumentoTrasero(fotoDocumentoTrasero);

        return responseDTO;
    }

}
