package com.gasber.appaddle.services;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.mappers.ReservaMapper;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.Cliente;
import com.gasber.appaddle.models.EstadoReserva;
import com.gasber.appaddle.models.Reserva;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.CanchaRepository;
import com.gasber.appaddle.repositories.ClienteRepository;
import com.gasber.appaddle.repositories.ReservaRepository;
import com.gasber.appaddle.repositories.AdministradorRepository;


import java.util.List;

@Service
public class ReservaService {
     private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final CanchaRepository canchaRepository;
    private final AdministradorRepository administradorRepository;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, CanchaRepository canchaRepository, AdministradorRepository administradorRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
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

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        // BUSCAR ADMINISTRADOR
        Administrador administrador = administradorRepository.findById(dto.getAdministradorId())
            .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        // Validar que la cancha esté libre para el rango horario
        if (!isCanchaDisponible(cancha, dto.getFechaHoraInicio(), dto.getDuracionMinutos())) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        Reserva reserva = ReservaMapper.toEntity(dto, cliente, cancha, administrador);
        
        reserva.setEstado(EstadoReserva.RESERVADA);  // Estado inicial
        
        Reserva guardada = reservaRepository.save(reserva);

        return ReservaMapper.toDTO(guardada);
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

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        if (!isCanchaDisponibleParaActualizacion(cancha, dto.getFechaHoraInicio(), dto.getDuracionMinutos(), id)) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        // Actualizo campos
        reservaExistente.setCliente(cliente);
        reservaExistente.setCancha(cancha);
        reservaExistente.setFechaHoraInicio(dto.getFechaHoraInicio());
        reservaExistente.setDuracionMinutos(dto.getDuracionMinutos());
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
        if (dto.getClienteId() == null) {
            throw new RuntimeException("El id del cliente es obligatorio");
        }
        if (dto.getCanchaId() == null) {
            throw new RuntimeException("El id de la cancha es obligatorio");
        }
        if (dto.getFechaHoraInicio() == null) {
            throw new RuntimeException("La fecha y hora de inicio es obligatoria");
        }
        if (dto.getDuracionMinutos() == null || dto.getDuracionMinutos() <= 0) {
            throw new RuntimeException("La duración debe ser mayor que 0");
        }
        if (dto.getAdministradorId() == null) {
            throw new RuntimeException("El id del administrador es obligatorio");
        }
    }

    // Verifica si la cancha está libre para una reserva nueva
        private boolean isCanchaDisponible(Cancha cancha, LocalDateTime inicio, int duracionMinutos) {
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);

        List<Reserva> reservasOcupadas = reservaRepository.findByCanchaAndEstadoIn(cancha, List.of(EstadoReserva.RESERVADA, EstadoReserva.EN_CURSO));

        for (Reserva r : reservasOcupadas) {
            LocalDateTime rInicio = r.getFechaHoraInicio();
            LocalDateTime rFin = rInicio.plusMinutes(r.getDuracionMinutos());

            // Chequeo si se solapan los intervalos
            if (inicio.isBefore(rFin) && fin.isAfter(rInicio)) {
                return false;
            }
        }
        return true;
    }

    // Similar a arriba pero excluye la reserva que se está actualizando (por id)
        private boolean isCanchaDisponibleParaActualizacion(Cancha cancha, LocalDateTime inicio, int duracionMinutos, Long reservaId) {
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);

        List<Reserva> reservasOcupadas = reservaRepository.findByCanchaAndEstadoIn(cancha, List.of(EstadoReserva.RESERVADA, EstadoReserva.EN_CURSO));

        for (Reserva r : reservasOcupadas) {
            if (r.getId().equals(reservaId)) continue;  // Excluye la reserva que actualizamos

            LocalDateTime rInicio = r.getFechaHoraInicio();
            LocalDateTime rFin = rInicio.plusMinutes(r.getDuracionMinutos());

            if (inicio.isBefore(rFin) && fin.isAfter(rInicio)) {
                return false;
            }
        }
        return true;
    }
    
}
