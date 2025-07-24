package org.example.zarp_back.controller;

import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.dto.imagen.ImagenResponseDTO;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.service.ImagenService;

public class ImagenController extends GenericoControllerImpl<Imagen, ImagenDTO, ImagenResponseDTO, Long, ImagenService> {

    public ImagenController(ImagenService servicio) {
        super(servicio);
    }

    // Aquí puedes agregar métodos específicos para el controlador de Imagen si es necesario
}
