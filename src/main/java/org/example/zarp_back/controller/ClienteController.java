package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.cliente.ClienteDTO;
import org.example.zarp_back.model.dto.cliente.ClienteResponseDTO;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends GenericoControllerImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long, ClienteService> {

    @Autowired
    private ClienteService clienteService;

    public ClienteController(ClienteService servicio) {
        super(servicio);
    }

    @PatchMapping("/verificacion-correo/{id}")
    public ResponseEntity<ClienteResponseDTO> verificacionCorreo(@PathVariable Long id) {
        ClienteResponseDTO response = clienteService.verificacionCorreo(id);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/verificacion-documento/{id}")
    public ResponseEntity<ClienteResponseDTO> verificacionDocumento(@PathVariable Long id, @RequestParam Boolean verificado, @RequestParam Long verificacionClienteId) {
        ClienteResponseDTO response = clienteService.verificacionDocumentacion(id, verificado, verificacionClienteId);
        return ResponseEntity.ok(response);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Cliente si es necesario
}
