package com.gasber.appaddle.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gasber.appaddle.dtos.AdministradorRequestDTO;
import com.gasber.appaddle.dtos.ApiResponse;
import com.gasber.appaddle.dtos.LoginRequestDTO;
import com.gasber.appaddle.dtos.LoginResponseDTO;
import com.gasber.appaddle.services.AdministradorService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse<String>> crearAdministrador(@RequestBody AdministradorRequestDTO dto) {
        administradorService.crearAdministrador(dto);
        ApiResponse<String> response = new ApiResponse<>(200, "Administrador creado exitosamente", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto) {
       // Llamamos al servicio, que devuelve usuario + token
    LoginResponseDTO loginResponse = administradorService.login(dto);
    
    // Creamos la respuesta genérica con el DTO completo
    ApiResponse<LoginResponseDTO> response = new ApiResponse<>(200, "Login exitoso", loginResponse);
    
    return ResponseEntity.ok(response);
    }
    
}
