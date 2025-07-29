package com.gasber.appaddle.services;

import com.gasber.appaddle.dtos.AdministradorRequestDTO;
import com.gasber.appaddle.dtos.LoginRequestDTO;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.AdministradorRepository;

import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public void crearAdministrador(AdministradorRequestDTO dto) {
        validarCampos(dto.getUsuario(), dto.getContraseña());

        if (administradorRepository.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Ya existe un administrador con ese usuario");
        }

        if (administradorRepository.count() > 0) {
            throw new RuntimeException("Ya hay un administrador registrado");
        }

        Administrador admin = new Administrador(dto.getUsuario(), dto.getContraseña());
        administradorRepository.save(admin);
    }

    public void login(LoginRequestDTO dto) {
        validarCampos(dto.getUsuario(), dto.getContraseña());

        Administrador admin = administradorRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario incorrecto"));

        if (!admin.getContraseña().equals(dto.getContraseña())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
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
