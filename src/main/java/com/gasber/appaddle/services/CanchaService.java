package com.gasber.appaddle.services;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gasber.appaddle.dtos.CanchaDTO;
import com.gasber.appaddle.mappers.CanchaMapper;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.repositories.CanchaRepository;

import java.util.List;


@Service
public class CanchaService {
    private final CanchaRepository canchaRepository;

    public CanchaService(CanchaRepository canchaRepository) {
        this.canchaRepository = canchaRepository;
    }

    public List<CanchaDTO> listarCanchasActivas() {
        return canchaRepository.findAll().stream()
                .filter(Cancha::isActiva)
                .map(CanchaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CanchaDTO crearCancha(CanchaDTO dto) {
        Cancha cancha = CanchaMapper.toEntity(dto);
        cancha.setActiva(true); // por defecto
        return CanchaMapper.toDTO(canchaRepository.save(cancha));
    }

    public CanchaDTO obtenerPorId(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));
        return CanchaMapper.toDTO(cancha);
    }

    public CanchaDTO actualizarCancha(Long id, CanchaDTO dto) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        cancha.setNombre(dto.getNombre());
        return CanchaMapper.toDTO(canchaRepository.save(cancha));
    }

    public void eliminarCancha(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));
        cancha.setActiva(false); // desactivamos la cancha en lugar de borrarla
        canchaRepository.save(cancha);
    }

    public List<CanchaDTO> listarTodas() {
        return canchaRepository.findAll().stream()
                .map(CanchaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
}
