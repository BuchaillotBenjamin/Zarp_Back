package org.example.zarp_back.service;

import org.example.zarp_back.config.exception.NotFoundException;
import org.example.zarp_back.model.entity.Base;
import org.example.zarp_back.model.interfaces.GenericoMapper;
import org.example.zarp_back.model.interfaces.GenericoRepository;
import org.example.zarp_back.model.interfaces.GenericoService;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericoServiceImpl<T extends Base,D,R,ID extends Serializable> implements GenericoService<T, D, R, ID> {

    protected GenericoRepository<T, ID> genericoRepository;
    protected GenericoMapper<T, D, R> genericoMapper;

    public GenericoServiceImpl(GenericoRepository<T, ID> genericoRepository, GenericoMapper<T, D, R> genericoMapper) {
        this.genericoRepository = genericoRepository;
        this.genericoMapper = genericoMapper;
    }

    @Override
    @Transactional
    public R save(D dto) {
        T entity = genericoMapper.toEntity(dto);
        genericoRepository.save(entity);
        return genericoMapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public R update(ID id, D dto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!genericoRepository.existsById(id)) {
            throw new NotFoundException("Entidad con el id " + id + " no encontrada");
        }
        genericoRepository.deleteById(id);

    }

    @Override
    public R findById(ID id) {
        T entity = genericoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entidad con el id " + id + " no encontrada"));
        return genericoMapper.toResponseDTO(entity);
    }

    @Override
    public List<R> findAll() {
        return genericoRepository.findAll().stream()
                .map(genericoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
