package org.example.zarp_back.service;

import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.config.mappers.ClienteMapper;
import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.dto.verificacionCliente.VerificacionClienteDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.VerificacionCliente;
import org.example.zarp_back.model.enums.AutorizacionesCliente;
import org.example.zarp_back.model.enums.Rol;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.repository.EmpleadoRepository;
import org.example.zarp_back.repository.VerificacionClienteRepository;
import org.example.zarp_back.service.utils.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ClienteService extends GenericoServiceImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long> {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private VerificacionClienteRepository verificacionClienteRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private VerificacionClienteService verificacionClienteService;
    @Autowired
    private NotificacionService notificacionService;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        super(clienteRepository, clienteMapper);
    }

    @Override
    @Transactional
    public ClienteResponseDTO save(ClienteDTO clienteDTO) {

        if (clienteRepository.existsByUid(clienteDTO.getUid())|| empleadoRepository.existsByUid(clienteDTO.getUid())) {
            log.error("El UID ya está en uso: {}", clienteDTO.getUid());
            throw new IllegalArgumentException("El UID ya está en uso");
        }

        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        //verificaciones
        cliente.setCorreoVerificado(false);
        cliente.setDocumentoVerificado(false);
        // Asignar rol por defecto
        cliente.setRol(Rol.CLIENTE);
        //credenciales
        cliente.setCredencialesMP(null);
        //autorizaciones
        cliente.setAutorizaciones(AutorizacionesCliente.NINGUNA);

        clienteRepository.save(cliente);



        log.info("Cliente guardado con éxito: {}", cliente.getId());
        return clienteMapper.toResponseDTO(cliente);
    }

    public boolean existsByUid(String uid) {
        return clienteRepository.existsByUid(uid);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update (Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));

        boolean updated = false;

        if (!clienteDTO.getNombreCompleto().equals(cliente.getNombreCompleto())) {
            cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
            updated = true;
        }

        if(!clienteDTO.getFotoPerfil().getUrlImagen().equals(cliente.getFotoPerfil().getUrlImagen())){
            cliente.getFotoPerfil().setUrlImagen(clienteDTO.getFotoPerfil().getUrlImagen());
            updated = true;
        }

        if (updated){
            clienteRepository.save(cliente);
            log.info("Cliente actualizado con éxito: {}", cliente.getId());
        }
        
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO verificacionCorreo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setCorreoVerificado(true);
        clienteRepository.save(cliente);
        log.info("Correo del cliente con id {} verificado", id);

        verificacionCompleta(id);

        return clienteMapper.toResponseDTO(cliente);

    }

    @Transactional
    public ClienteResponseDTO verificacionDocumentacion(Long id, boolean verificado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));

        VerificacionCliente verificacionCliente = verificacionClienteRepository.findVerificacionActivaByClienteId(id)
                .orElseThrow(() -> new NotFoundException("No hay verificación activa para el cliente con id: " + id));

        if (verificado) {
            cliente.setDocumentoVerificado(true);
            clienteRepository.save(cliente);
            verificacionClienteService.toggleActivo(verificacionCliente.getId());
            log.info("Documentación del cliente con id {} verificada", id);
            if(cliente.getCorreoVerificado()){
                notificacionService.notifcarVerificacionDocumento(cliente.getId());
            }
            verificacionCompleta(id);
        }else{
            verificacionClienteService.toggleActivo(verificacionCliente.getId());
            log.warn("Documentación del cliente con id {} no verificada, verificación desactivada", id);
            if (cliente.getCorreoVerificado()){
                notificacionService.notificarRechazoDocumento(cliente.getId());
            }
        }

        return clienteMapper.toResponseDTO(cliente);

    }

    public ClienteResponseDTO getByUid(String uid) {
        Cliente cliente = clienteRepository.findByUid(uid)
                .orElseThrow(() -> new NotFoundException("Cliente con el UID " + uid + " no encontrado"));

        return clienteMapper.toResponseDTO(cliente);
    }

    public void actualizarAutorizaciones(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + clienteId));
        Boolean updated = false;

        if (cliente.getAutorizaciones() == AutorizacionesCliente.AMBAS) {
            return; // Ya tiene la autorización, no hacer nada
        }
        if(cliente.getAutorizaciones() == AutorizacionesCliente.NINGUNA){

            if (cliente.getCredencialesMP()!=null && cliente.getCredencialesPP()!=null){
                cliente.setAutorizaciones(AutorizacionesCliente.AMBAS);
                updated = true;
            }
            if(cliente.getCredencialesMP()!=null&& updated==false){
                cliente.setAutorizaciones(AutorizacionesCliente.MERCADO_PAGO);
                updated = true;
            }
            if (cliente.getCredencialesPP()!=null && updated==false){
                cliente.setAutorizaciones(AutorizacionesCliente.PAYPAL);
                updated = true;
            }


        }
        if (updated){
            clienteRepository.save(cliente);
            log.info("Autorizaciones del cliente con id {} actualizadas a {}", clienteId, cliente.getAutorizaciones());
        }

    }

    private void verificacionCompleta(long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + clienteId));

        if (cliente.getCorreoVerificado() && cliente.getDocumentoVerificado()) {
            cliente.setRol(Rol.PROPIETARIO);
            clienteRepository.save(cliente);
            log.info("Cliente con id {} verificado completamente y rol actualizado a PROPIETARIO", clienteId);
        }

    }


    // Aquí puedes agregar métodos específicos para el servicio de Cliente si es necesario
}
