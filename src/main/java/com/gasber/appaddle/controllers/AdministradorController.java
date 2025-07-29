package com.gasber.appaddle.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gasber.appaddle.dtos.AdministradorRequestDTO;
import com.gasber.appaddle.dtos.LoginRequestDTO;
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
    public ResponseEntity<String> crearAdministrador(@RequestBody AdministradorRequestDTO dto) {
        administradorService.crearAdministrador(dto);
        return ResponseEntity.ok("Administrador creado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto) {
        administradorService.login(dto);
        return ResponseEntity.ok("Login exitoso");
    }
    
}
