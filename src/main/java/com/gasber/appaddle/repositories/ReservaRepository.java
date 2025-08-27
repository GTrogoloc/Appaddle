package com.gasber.appaddle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.EstadoReserva;
import com.gasber.appaddle.models.Reserva;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    // Busca todas las reservas para una cancha en estado RESERVADA o EN_CURSO (es decir, ocupadas)
    List<Reserva> findByCanchaAndEstadoIn(Cancha cancha, List<EstadoReserva> estados);
    
    // Busca todas las reservas de un administrador
    List<Reserva> findByAdministrador(Administrador administrador);
}
