package com.gasber.appaddle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.services.ReservaService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Listar todas las reservas
    @GetMapping
    public List<ReservaDTO> listarTodas() {
        return reservaService.listarTodas();
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ReservaDTO obtenerPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id);
    }

    // Crear una nueva reserva
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaDTO crearReserva(@RequestBody ReservaRequestDTO dto) {
        return reservaService.crearReserva(dto);
    }

    // Actualizar una reserva existente
    @PutMapping("/{id}")
    public ReservaDTO actualizarReserva(@PathVariable Long id, @RequestBody ReservaRequestDTO dto) {
        return reservaService.actualizarReserva(id, dto);
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
    }
    
}
