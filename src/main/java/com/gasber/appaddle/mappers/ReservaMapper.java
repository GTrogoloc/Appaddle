package com.gasber.appaddle.mappers;

import com.gasber.appaddle.dtos.AdministradorDTO;
import com.gasber.appaddle.dtos.CanchaDTO;
import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.Reserva;
import com.gasber.appaddle.models.Administrador;

public class ReservaMapper {

    public static ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setNombre(reserva.getNombre());
        dto.setApellido(reserva.getApellido());
        dto.setTelefono(reserva.getTelefono());
        dto.setFechaHoraInicio(reserva.getFechaHoraInicio());
        dto.setDuracionMinutos(reserva.getDuracionMinutos());
        dto.setEstado(reserva.getEstado());


        // CanchaDTO (solo id, nombre) ----
        CanchaDTO canchaDTO = new CanchaDTO();
        canchaDTO.setId(reserva.getCancha().getId());
        canchaDTO.setNombre(reserva.getCancha().getNombre());
        canchaDTO.setActiva(reserva.getCancha().isActiva());
        dto.setCancha(canchaDTO);

        // AdministradorDTO (solo id, usuario) ----
        AdministradorDTO adminDTO = new AdministradorDTO();
        adminDTO.setId(reserva.getAdministrador().getId());
        adminDTO.setUsuario(reserva.getAdministrador().getUsuario());
        dto.setAdministrador(adminDTO);

        return dto;
    }

    public static Reserva toEntity(ReservaRequestDTO dto, Cancha cancha, Administrador administrador) {
        Reserva reserva = new Reserva();
        reserva.setNombre(dto.getNombre());
        reserva.setApellido(dto.getApellido());
        reserva.setTelefono(dto.getTelefono());
        reserva.setFechaHoraInicio(dto.getFechaHoraInicio());
        reserva.setDuracionMinutos(dto.getDuracionMinutos());
        reserva.setCancha(cancha);
        reserva.setAdministrador(administrador);

        return reserva;
    }
    
}
