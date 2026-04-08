package com.gasber.appaddle.services;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.mappers.ReservaMapper;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.EstadoReserva;
import com.gasber.appaddle.models.Reserva;
import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.CanchaRepository;
import com.gasber.appaddle.repositories.ReservaRepository;
import com.gasber.appaddle.security.JwtUtil;
import com.gasber.appaddle.repositories.AdministradorRepository;
import com.gasber.appaddle.services.ConfiguracionPagosService;
import com.gasber.appaddle.models.ConfiguracionPagos;
import com.gasber.appaddle.models.EstadoPago;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final AdministradorRepository administradorRepository;
    private final JwtUtil jwtUtil;
    private final ConfiguracionPagosService configuracionPagosService;

    private static final int DURACION_FIJA_MINUTOS = 90; // Duración fija

    public ReservaService(ReservaRepository reservaRepository, CanchaRepository canchaRepository, AdministradorRepository administradorRepository,
            JwtUtil jwtUtil, ConfiguracionPagosService configuracionPagosService) {
        this.reservaRepository = reservaRepository;
        this.canchaRepository = canchaRepository;
        this.administradorRepository = administradorRepository;
        this.jwtUtil = jwtUtil;
        this.configuracionPagosService = configuracionPagosService;
    }

    // Método privado para token
    private Administrador obtenerAdministradorDesdeToken(String token) {
        if (!jwtUtil.esValido(token)) {
            throw new RuntimeException("Token inválido");
        }

        String usuarioAdmin = jwtUtil.obtenerUsuarioDelToken(token);

        return administradorRepository.findByUsuario(usuarioAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
    }

    // Listar todas las reservas de un admin
    public List<ReservaDTO> listarReservasPorAdmin(String token) {
        Administrador admin = obtenerAdministradorDesdeToken(token);
    
        return reservaRepository.findAll().stream()
            .filter(r -> r.getAdministrador().getId().equals(admin.getId()))
            .map(ReservaMapper::toDTO)
            .collect(Collectors.toList());
    }


    // Listar todas las reservas
    public List<ReservaDTO> listarTodas() {
        return reservaRepository.findAll().stream()
            .map(ReservaMapper::toDTO)
            .collect(Collectors.toList());
    }

    // Crear reserva (validando disponibilidad)
        public ReservaDTO crearReserva(ReservaRequestDTO dto, String token) {
        validarReserva(dto);

        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        Administrador admin = obtenerAdministradorDesdeToken(token);

             // Truncar a minutos
             LocalDateTime inicio = dto.getFechaHoraInicio().truncatedTo(ChronoUnit.MINUTES);

        // Validar que la cancha esté libre para el rango horario
        if (!isCanchaDisponible(cancha, inicio)) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        LocalDateTime finReserva = inicio.plusMinutes(DURACION_FIJA_MINUTOS);

        if (!finReserva.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden crear reservas en horarios ya finalizados");
        }

        Reserva reserva = new Reserva();
        reserva.setNombre(dto.getNombre());
        reserva.setApellido(dto.getApellido());
        reserva.setTelefono(dto.getTelefono());
        reserva.setCancha(cancha);
        reserva.setAdministrador(admin);
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(inicio.plusMinutes(DURACION_FIJA_MINUTOS));
        reserva.setEstado(EstadoReserva.RESERVADA);

        ConfiguracionPagos config = configuracionPagosService.obtenerConfiguracion();

reserva.setPrecioTotal(config.getPrecioTurno());
reserva.setSeniaTotal(config.getSeniaTurno());

LocalDateTime ahora = LocalDateTime.now();

reserva.setFechaCreacion(ahora);

reserva.setFechaLimitePago(
        reserva.getFechaHoraInicio().minusHours(config.getHorasCancelacion())
);
        
        Reserva guardada = reservaRepository.save(reserva);

        return ReservaMapper.toDTO(guardada); // fechaHoraFin se calcula en el mapper
    }

    // Obtener reserva por ID
    public ReservaDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return ReservaMapper.toDTO(reserva);
    }

    // Actualizar reserva (validando disponibilidad, excluyendo la reserva que se actualiza)
    public ReservaDTO actualizarReserva(Long id, ReservaRequestDTO dto, String token) {
        validarReserva(dto);
        
        // Obtener admin desde token
        String usuarioAdmin = jwtUtil.obtenerUsuarioDelToken(token);
        Administrador admin = administradorRepository.findByUsuario(usuarioAdmin)
            .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        
        // Obtener reserva existente
        Reserva reservaExistente = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

         // Validar que la reserva pertenece al admin
        if (!reservaExistente.getAdministrador().getId().equals(admin.getId())) {
        throw new RuntimeException("No podés actualizar una reserva que no es tuya");
        }

        Cancha cancha = canchaRepository.findById(dto.getCanchaId())
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));
           
        LocalDateTime inicio = dto.getFechaHoraInicio().truncatedTo(ChronoUnit.MINUTES);
        
        //validar disponibilidad de la cancha, excluyendo la reserva actual
        if (!isCanchaDisponibleParaActualizacion(cancha, inicio, id)) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        // Actualizo campos
        reservaExistente.setNombre(dto.getNombre());
        reservaExistente.setApellido(dto.getApellido());
        reservaExistente.setTelefono(dto.getTelefono());
        reservaExistente.setCancha(cancha);
        reservaExistente.setFechaHoraInicio(inicio);
        reservaExistente.setFechaHoraFin(inicio.plusMinutes(DURACION_FIJA_MINUTOS));

        Reserva actualizada = reservaRepository.save(reservaExistente);
        return ReservaMapper.toDTO(actualizada);
    }

    // Eliminar reserva
    @Transactional
    public void eliminarReserva(Long id, String token) {
  
        // Obtener admin desde token
    String usuarioAdmin = jwtUtil.obtenerUsuarioDelToken(token);
    Administrador admin = administradorRepository.findByUsuario(usuarioAdmin)
        .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

    // Obtener reserva
    Reserva reserva = reservaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    // Validar que pertenece al admin
    if (!reserva.getAdministrador().getId().equals(admin.getId())) {
        throw new RuntimeException("No podés eliminar una reserva que no es tuya");
    }

    reserva.setEstado(EstadoReserva.CANCELADA);
    reserva.setFechaCancelacion(LocalDateTime.now());

    reservaRepository.save(reserva);
    }

    // MARCAR SEÑA PAGADA DE LA RESERVA
    public ReservaDTO marcarSeniaPagada(Long id, String token) {

    Administrador admin = obtenerAdministradorDesdeToken(token);

    Reserva reserva = reservaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    if (!reserva.getAdministrador().getId().equals(admin.getId())) {
        throw new RuntimeException("No podés modificar una reserva que no es tuya");
    }

    if (reserva.getEstado() != EstadoReserva.RESERVADA) {
        throw new RuntimeException("La reserva no está en estado RESERVADA");
    }

    reserva.setEstadoPago(EstadoPago.SENIA_PAGADA);

    Reserva guardada = reservaRepository.save(reserva);

    return ReservaMapper.toDTO(guardada);
}

   // MARCAR PAGO COMPLETO DE LA RESERVA 
   public ReservaDTO marcarPagoCompleto(Long id, String token) {

    Administrador admin = obtenerAdministradorDesdeToken(token);

    Reserva reserva = reservaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    if (!reserva.getAdministrador().getId().equals(admin.getId())) {
        throw new RuntimeException("No podés modificar una reserva que no es tuya");
    }

    if (reserva.getEstado() != EstadoReserva.RESERVADA) {
        throw new RuntimeException("La reserva no está en estado RESERVADA");
    }

    reserva.setEstadoPago(EstadoPago.PAGADO);

    Reserva guardada = reservaRepository.save(reserva);

    return ReservaMapper.toDTO(guardada);
}


    
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

    //Scheduled para actualizar el estado de las reservas
    @Scheduled(fixedRate = 600000) // cada 10 minutos
    public void cancelarReservasPendientes() {

    List<Reserva> reservas = reservaRepository.findByEstadoPagoAndEstado(
            EstadoPago.PENDIENTE,
            EstadoReserva.RESERVADA
    );

    LocalDateTime ahora = LocalDateTime.now();

    for (Reserva r : reservas) {

        if (r.getFechaLimitePago() != null &&
            ahora.isAfter(r.getFechaLimitePago())) {

            r.setEstado(EstadoReserva.CANCELADA);
            r.setFechaCancelacion(ahora);

            reservaRepository.save(r);
        }
    }
}
    
}

