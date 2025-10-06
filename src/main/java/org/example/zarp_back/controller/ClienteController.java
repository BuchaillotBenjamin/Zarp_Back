package org.example.zarp_back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.service.AuditoriaService;
import org.example.zarp_back.service.ClienteService;
import org.example.zarp_back.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@Slf4j
public class ClienteController extends GenericoControllerImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long, ClienteService> {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AuditoriaService auditoriaService;

    @Override
    protected String entidadNombre() {
        return "clientes";
    }

    public ClienteController(ClienteService servicio) {
        super(servicio);
    }

    @PatchMapping("/verificacion-correo/{id}")
    public ResponseEntity<ClienteResponseDTO> verificacionCorreo(@PathVariable Long id, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} ejecutó verificación de correo para cliente ID {}", uid, id);

        ClienteResponseDTO response = clienteService.verificacionCorreo(id);
        messagingTemplate.convertAndSend("/topic/clientes/update", response);
        auditoriaService.registrar(uid,entidadNombre(),"VERIFICACION_CORREO",id.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/verificacion-documento/{id}")
    public ResponseEntity<ClienteResponseDTO> verificacionDocumento(@PathVariable Long id, @RequestParam Boolean verificado, HttpServletRequest request) {
        String uid = (String) request.getAttribute("firebaseUid");
        log.info("UID: {} ejecutó verificación de documento para cliente ID {} con estado {}", uid, id, verificado);

        ClienteResponseDTO response = clienteService.verificacionDocumentacion(id, verificado);

        if (verificado){
            messagingTemplate.convertAndSend("/topic/clientes/update", response);
        }
        auditoriaService.registrar(uid,entidadNombre(),verificado ? "VERIFICACION-DOCUMENTO-ACEPTADA" : "VERIFICACION-DOCUMENTO-RECHAZADA" ,id.toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/existe-uid/{uid}")
    public ResponseEntity<Boolean> existsByUid(@PathVariable String uid) {

        Boolean exists = clienteService.existsByUid(uid);

        return ResponseEntity.ok(exists);

    }

    @GetMapping("/existe-uidLogin/{uid}")
    public ResponseEntity<Boolean> existsByUidLogin(@PathVariable String uid) {

        Boolean exists = clienteService.existsByUid(uid);

        if (!exists) {
            exists = empleadoService.existsByUid(uid);
        }

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getByUid/{uid}")
    public ResponseEntity<ClienteResponseDTO> getByUid(@PathVariable String uid) {

        ClienteResponseDTO cliente = clienteService.getByUid(uid);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/getByUidLogin/{uid}")
    public ResponseEntity<ClienteResponseDTO> getByUidLogin(@PathVariable String uid) {

        ClienteResponseDTO cliente = null;
        try {
           cliente = clienteService.getByUid(uid);
           return ResponseEntity.ok(cliente);
        }catch (NotFoundException e) {
            try{
                cliente = empleadoService.getByUidLogin(uid);
                return ResponseEntity.ok(cliente);
            }catch (NotFoundException ex){
                throw new NotFoundException("No se encontró un cliente o empleado con el UID: " + uid);
            }
        }
    }

    // Aquí puedes agregar métodos específicos para el controlador de Cliente si es necesario
}
