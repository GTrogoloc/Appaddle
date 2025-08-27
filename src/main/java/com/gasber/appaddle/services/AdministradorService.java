package com.gasber.appaddle.services;

import com.gasber.appaddle.dtos.AdministradorRequestDTO;
import com.gasber.appaddle.dtos.LoginRequestDTO;
import com.gasber.appaddle.dtos.LoginResponseDTO;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.AdministradorRepository;
import com.gasber.appaddle.security.JwtUtil;

import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final JwtUtil jwtUtil;

    public AdministradorService(AdministradorRepository administradorRepository, JwtUtil jwtUtil) {
        this.administradorRepository = administradorRepository;
        this.jwtUtil = jwtUtil;
    }

    public void crearAdministrador(AdministradorRequestDTO dto) {
        validarCampos(dto.getUsuario(), dto.getContraseña());

        if (administradorRepository.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Ya existe un administrador con ese usuario");
        }

        Administrador admin = new Administrador(dto.getUsuario(), dto.getContraseña());
        administradorRepository.save(admin);
    }

    //Nuevo metodo para generar el token
    public LoginResponseDTO login(LoginRequestDTO dto) {
        validarCampos(dto.getUsuario(), dto.getContraseña());
        
        Administrador admin = administradorRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario incorrecto"));

        if (!admin.getContraseña().equals(dto.getContraseña())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
    
        String token = jwtUtil.generarToken(admin.getUsuario());
        return new LoginResponseDTO(admin.getUsuario(), token);
}

    private void validarCampos(String usuario, String contraseña) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario no puede estar vacío");
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }
    }
    
}
