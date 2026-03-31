package com.gasber.appaddle.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@CrossOrigin(origins = "*")
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

    //Mis reservas
    @GetMapping("/mis-reservas")
    public ResponseEntity<ApiResponse<List<ReservaDTO>>> listarReservasPorAdmin(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.replace("Bearer ", "");
        List<ReservaDTO> reservas = reservaService.listarReservasPorAdmin(token);
        ApiResponse<List<ReservaDTO>> response = new ApiResponse<>(
            200,
            "Reservas del administrador",
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
    public ResponseEntity<ApiResponse<ReservaDTO>> crearReserva(
        @RequestBody ReservaRequestDTO dto,
        @RequestHeader("Authorization") String authHeader) {
            String token = authHeader.replace("Bearer ", "");

        ReservaDTO reserva = reservaService.crearReserva(dto, token);
        ApiResponse<ReservaDTO> response = new ApiResponse<>(
            201,
            "Reserva creada exitosamente",
            reserva
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Actualizar una reserva existente (Requiere token)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaDTO>> actualizarReserva(
        @PathVariable Long id,
        @RequestBody ReservaRequestDTO dto,
        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        ReservaDTO reservaActualizada = reservaService.actualizarReserva(id, dto, token);
        ApiResponse<ReservaDTO> response = new ApiResponse<>(
            200,
            "Reserva actualizada exitosamente",
            reservaActualizada
        );
        return ResponseEntity.ok(response);
    }

// Cancelar una reserva (NO se borra)
@PutMapping("/{id}/cancelar")
public ResponseEntity<ApiResponse<String>> cancelarReserva(
    @PathVariable Long id,
    @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.replace("Bearer ", "");
    reservaService.eliminarReserva(id, token);

    ApiResponse<String> response = new ApiResponse<>(
        200,
        "Reserva cancelada exitosamente",
        null
    );
    return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/senia")
public ResponseEntity<ApiResponse<ReservaDTO>> marcarSenia(
    @PathVariable Long id,
    @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.replace("Bearer ", "");

    ReservaDTO reserva = reservaService.marcarSeniaPagada(id, token);

    ApiResponse<ReservaDTO> response = new ApiResponse<>(
        200,
        "Seña registrada correctamente",
        reserva
    );

    return ResponseEntity.ok(response);
}

@PutMapping("/{id}/pagar")
public ResponseEntity<ApiResponse<ReservaDTO>> marcarPago(
    @PathVariable Long id,
    @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.replace("Bearer ", "");

    ReservaDTO reserva = reservaService.marcarPagoCompleto(id, token);

    ApiResponse<ReservaDTO> response = new ApiResponse<>(
        200,
        "Pago registrado correctamente",
        reserva
    );

    return ResponseEntity.ok(response);
}

}

