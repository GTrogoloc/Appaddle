package com.gasber.appaddle.mappers;

import com.gasber.appaddle.dtos.AdministradorDTO;
import com.gasber.appaddle.dtos.CanchaDTO;
import com.gasber.appaddle.dtos.ClienteDTO;
import com.gasber.appaddle.dtos.ReservaDTO;
import com.gasber.appaddle.dtos.ReservaRequestDTO;
import com.gasber.appaddle.models.Cancha;
import com.gasber.appaddle.models.Cliente;
import com.gasber.appaddle.models.Reserva;
import com.gasber.appaddle.models.Administrador;

public class ReservaMapper {
    public static ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setFechaHoraInicio(reserva.getFechaHoraInicio());
        dto.setDuracionMinutos(reserva.getDuracionMinutos());
        dto.setEstado(reserva.getEstado());

        // ClienteDTO (solo id, nombre, apellido) ----
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(reserva.getCliente().getId());
        clienteDTO.setNombre(reserva.getCliente().getNombre());
        clienteDTO.setApellido(reserva.getCliente().getApellido());
        clienteDTO.setTelefono(reserva.getCliente().getTelefono());
        dto.setCliente(clienteDTO);

        // CanchaDTO (solo id, nombre) ----
        CanchaDTO canchaDTO = new CanchaDTO();
        canchaDTO.setId(reserva.getCancha().getId());
        canchaDTO.setNombre(reserva.getCancha().getNombre());
        dto.setCancha(canchaDTO);

        // AdministradorDTO (solo id, usuario) ----
        AdministradorDTO adminDTO = new AdministradorDTO();
        adminDTO.setId(reserva.getAdministrador().getId());
        adminDTO.setUsuario(reserva.getAdministrador().getUsuario());
        dto.setAdministrador(adminDTO);

        return dto;
    }

    public static Reserva toEntity(ReservaRequestDTO dto, Cliente cliente, Cancha cancha, Administrador administrador) {
        Reserva reserva = new Reserva();
        reserva.setFechaHoraInicio(dto.getFechaHoraInicio());
        reserva.setDuracionMinutos(dto.getDuracionMinutos());
        reserva.setCliente(cliente);
        reserva.setCancha(cancha);
        reserva.setAdministrador(administrador);

        return reserva;
    }
    
}
