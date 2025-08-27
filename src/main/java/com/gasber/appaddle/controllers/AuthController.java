package com.gasber.appaddle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gasber.appaddle.dtos.LoginResponseDTO;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.AdministradorRepository;
import com.gasber.appaddle.security.JwtUtil;
import org.springframework.web.bind.annotation.RequestBody;

import com.gasber.appaddle.dtos.LoginRequestDTO;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
   @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Administrador admin = administradorRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        if (!admin.getContraseña().equals(request.getContraseña())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(admin.getUsuario());
        LoginResponseDTO response = new LoginResponseDTO(admin.getUsuario(), token);
        return ResponseEntity.ok(response);
    }
}
