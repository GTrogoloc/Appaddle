package com.gasber.appaddle.mappers;

import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.Cliente;
import com.gasber.appaddle.models.Reserva;

public class ReservaMapper {
    public static ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setFechaHoraInicio(reserva.getFechaHoraInicio());
        dto.setDuracionMinutos(reserva.getDuracionMinutos());
        dto.setEstado(reserva.getEstado());
        dto.setClienteId(reserva.getCliente().getId());
        dto.setClienteNombre(reserva.getCliente().getNombre());
        dto.setClienteApellido(reserva.getCliente().getApellido());
        dto.setCanchaId(reserva.getCancha().getId());
        dto.setCanchaNombre(reserva.getCancha().getNombre());
        return dto;
    }

    public static Reserva toEntity(ReservaRequestDTO dto, Cliente cliente, Cancha cancha) {
        Reserva reserva = new Reserva();
        reserva.setFechaHoraInicio(dto.getFechaHoraInicio());
        reserva.setDuracionMinutos(dto.getDuracionMinutos());
        reserva.setCliente(cliente);
        reserva.setCancha(cancha);
        return reserva;
    }
    
}
