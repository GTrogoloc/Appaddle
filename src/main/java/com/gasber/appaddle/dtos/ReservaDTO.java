package com.gasber.appaddle.dtos;

import java.time.LocalDateTime;
import com.gasber.appaddle.models.EstadoReserva;

public class ReservaDTO {
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private Integer duracionMinutos;
    private EstadoReserva estado;
    private CanchaDTO cancha;
    private ClienteDTO cliente;
    private AdministradorDTO administrador;

    public ReservaDTO() {}

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public CanchaDTO getCancha() {
        return cancha;
    }

    public void setCancha(CanchaDTO cancha) {
        this.cancha = cancha;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public AdministradorDTO getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorDTO administrador) {
        this.administrador = administrador;
    }

    
}
