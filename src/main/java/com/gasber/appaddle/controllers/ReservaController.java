package com.gasber.appaddle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gasber.appaddle.dtos.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ReservaDTO>>> listarTodas() {
        List<ReservaDTO> reservas = reservaService.listarTodas();
        ApiResponse<List<ReservaDTO>> response = new ApiResponse<>(
            200,
            "Todas las reservas",
            reservas
        );
        return ResponseEntity.ok(response);
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaDTO>> obtenerPorId(@PathVariable Long id) {
        ReservaDTO reserva = reservaService.obtenerPorId(id);
        ApiResponse<ReservaDTO> response = new ApiResponse<>(
            200,
            "Reserva encontrada",
            reserva
        );
        return ResponseEntity.ok(response);
    }

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<ApiResponse<ReservaDTO>> crearReserva(@RequestBody ReservaRequestDTO dto) {
        ReservaDTO reserva = reservaService.crearReserva(dto);
        ApiResponse<ReservaDTO> response = new ApiResponse<>(
            201,
            "Reserva creada exitosamente",
            reserva
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Actualizar una reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaDTO>> actualizarReserva(@PathVariable Long id, @RequestBody ReservaRequestDTO dto) {
        ReservaDTO reservaActualizada = reservaService.actualizarReserva(id, dto);
        ApiResponse<ReservaDTO> response = new ApiResponse<>(
            200,
            "Reserva actualizada exitosamente",
            reservaActualizada
        );
        return ResponseEntity.ok(response);
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        ApiResponse<String> response = new ApiResponse<>(
            200,
            "Reserva eliminada exitosamente",
            null
        );
        return ResponseEntity.ok(response);
    }
    }

