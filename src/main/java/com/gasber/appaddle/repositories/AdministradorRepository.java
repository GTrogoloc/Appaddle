package com.gasber.appaddle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gasber.appaddle.models.Administrador;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
     Optional<Administrador> findByUsuario(String usuario);
    boolean existsByUsuario(String usuario);
    
}
