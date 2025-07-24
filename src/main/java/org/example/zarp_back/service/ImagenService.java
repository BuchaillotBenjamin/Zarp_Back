package org.example.zarp_back.service;

import org.example.zarp_back.config.mappers.ImagenMapper;
import org.example.zarp_back.model.dto.imagen.ImagenDTO;
import org.example.zarp_back.model.dto.imagen.ImagenResponseDTO;
import org.example.zarp_back.model.entity.Imagen;
import org.example.zarp_back.repository.ImagenRepository;

public class ImagenService extends GenericoServiceImpl<Imagen, ImagenDTO, ImagenResponseDTO, Long> {

    public ImagenService(ImagenRepository imagenRepository, ImagenMapper imagenMapper) {
        super(imagenRepository, imagenMapper);
    }

    // Aquí puedes agregar métodos específicos para el servicio de Imagen si es necesario
}
