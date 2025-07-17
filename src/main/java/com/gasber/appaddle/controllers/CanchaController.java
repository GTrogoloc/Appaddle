package com.gasber.appaddle.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.gasber.appaddle.dtos.CanchaDTO;
import com.gasber.appaddle.services.CanchaService;
import java.util.List;

@RestController
@RequestMapping("/canchas")
public class CanchaController {
    private final CanchaService canchaService;

    public CanchaController(CanchaService canchaService) {
        this.canchaService = canchaService;
    }

    @GetMapping
    public List<CanchaDTO> listarCanchasActivas() {
        return canchaService.listarCanchasActivas();
    }

    @GetMapping("/todas")
    public List<CanchaDTO> listarTodas() {
        return canchaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CanchaDTO obtenerPorId(@PathVariable Long id) {
        return canchaService.obtenerPorId(id);
    }

    @PostMapping
    public CanchaDTO crear(@RequestBody CanchaDTO dto) {
        return canchaService.crearCancha(dto);
    }

    @PutMapping("/{id}")
    public CanchaDTO actualizar(@PathVariable Long id, @RequestBody CanchaDTO dto) {
        return canchaService.actualizarCancha(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        canchaService.eliminarCancha(id);
    }
    
}
