package com.gasber.appaddle.services;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.mappers.ReservaMapper;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.EstadoReserva;
import com.gasber.appaddle.models.Reserva;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.CanchaRepository;
import com.gasber.appaddle.repositories.ReservaRepository;
import com.gasber.appaddle.repositories.AdministradorRepository;

import java.time.temporal.ChronoUnit;


import java.util.List;

@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final AdministradorRepository administradorRepository;

    private static final int DURACION_FIJA_MINUTOS = 90; // 🔹 Duración fija

    public ReservaService(ReservaRepository reservaRepository, CanchaRepository canchaRepository, AdministradorRepository administradorRepository) {
        this.reservaRepository = reservaRepository;
        this.canchaRepository = canchaRepository;
        this.administradorRepository = administradorRepository;
    }

    // 1. Listar todas las reservas (DTOs)
    public List<ReservaDTO> listarTodas() {
        return reservaRepository.findAll().stream()
            .map(ReservaMapper::toDTO)
            .collect(Collectors.toList());
    }

    // 2. Crear reserva (validando disponibilidad)
        public ReservaDTO crearReserva(ReservaRequestDTO dto) {
        validarReserva(dto);

        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        // BUSCAR ADMINISTRADOR
        Administrador administrador = administradorRepository.findById(dto.getAdministradorId())
            .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

             // Truncar a minutos
             LocalDateTime inicio = dto.getFechaHoraInicio().truncatedTo(ChronoUnit.MINUTES);

        // Validar que la cancha esté libre para el rango horario
        if (!isCanchaDisponible(cancha, dto.getFechaHoraInicio())) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        Reserva reserva = new Reserva();
        reserva.setNombre(dto.getNombre());
        reserva.setApellido(dto.getApellido());
        reserva.setTelefono(dto.getTelefono());
        reserva.setCancha(cancha);
        reserva.setAdministrador(administrador);
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(inicio.plusMinutes(DURACION_FIJA_MINUTOS));
        reserva.setEstado(EstadoReserva.RESERVADA);  // Estado inicial
        
        Reserva guardada = reservaRepository.save(reserva);

        return ReservaMapper.toDTO(guardada); // fechaHoraFin se calcula en el mapper
    }

    // 3. Obtener reserva por ID
    public ReservaDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return ReservaMapper.toDTO(reserva);
    }

    // 4. Actualizar reserva (validando disponibilidad, excluyendo la reserva que se actualiza)
    public ReservaDTO actualizarReserva(Long id, ReservaRequestDTO dto) {
        validarReserva(dto);

        Reserva reservaExistente = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));


        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));
           
            LocalDateTime inicio = dto.getFechaHoraInicio().truncatedTo(ChronoUnit.MINUTES);

        if (!isCanchaDisponibleParaActualizacion(cancha, dto.getFechaHoraInicio(), id)) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        // Actualizo campos
        reservaExistente.setNombre(dto.getNombre());
        reservaExistente.setApellido(dto.getApellido());
        reservaExistente.setTelefono(dto.getTelefono());
        reservaExistente.setCancha(cancha);
        reservaExistente.setFechaHoraInicio(inicio);
        reservaExistente.setFechaHoraFin(inicio.plusMinutes(DURACION_FIJA_MINUTOS));
        // Opcional: actualizar estado si querés, o dejarlo igual

        Reserva actualizada = reservaRepository.save(reservaExistente);
        return ReservaMapper.toDTO(actualizada);
    }

    // 5. Eliminar reserva
    public void eliminarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reservaRepository.delete(reserva);
    }

    // ----------------------------
    // Métodos privados

    // Validar campos mínimos
    private void validarReserva(ReservaRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (dto.getApellido() == null || dto.getApellido().isBlank()){
            throw new RuntimeException("El Apellido es obligatorio");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()){
            throw new RuntimeException("El telefono es obligatorio");
        }
        if (dto.getCanchaId() == null) {
            throw new RuntimeException("El id de la cancha es obligatorio");
        }
        if (dto.getFechaHoraInicio() == null) {
            throw new RuntimeException("La fecha y hora de inicio es obligatoria");
        }
        if (dto.getAdministradorId() == null) {
            throw new RuntimeException("El id del administrador es obligatorio");
        }
    }

   
   // Verifica si la cancha está libre para un nuevo turno
    private boolean isCanchaDisponible(Cancha cancha, LocalDateTime inicio) {
    LocalDateTime fin = inicio.plusMinutes(DURACION_FIJA_MINUTOS);

    List<Reserva> reservasOcupadas = reservaRepository.findByCanchaAndEstadoIn(
            cancha, List.of(EstadoReserva.RESERVADA, EstadoReserva.EN_CURSO));

    for (Reserva r : reservasOcupadas) {
        if (inicio.isBefore(r.getFechaHoraFin()) && fin.isAfter(r.getFechaHoraInicio())) {
            return false; // se solapan
        }
    }
    return true;
}

    // Verifica disponibilidad para actualización (excluyendo la reserva actual)
    private boolean isCanchaDisponibleParaActualizacion(Cancha cancha, LocalDateTime inicio, Long reservaId) {
        LocalDateTime fin = inicio.plusMinutes(DURACION_FIJA_MINUTOS);

        List<Reserva> reservasOcupadas = reservaRepository.findByCanchaAndEstadoIn(
                cancha, List.of(EstadoReserva.RESERVADA, EstadoReserva.EN_CURSO));

        for (Reserva r : reservasOcupadas) {
            if (r.getId().equals(reservaId)) continue; // excluye reserva actual
            if (inicio.isBefore(r.getFechaHoraFin()) && fin.isAfter(r.getFechaHoraInicio())) {
                return false; // se solapan
            }
        }
        return true;
    }
    
}
