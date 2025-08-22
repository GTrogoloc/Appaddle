package com.gasber.appaddle.dtos;

import java.time.LocalDateTime;
import com.gasber.appaddle.models.EstadoReserva;

public class ReservaDTO {
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDateTime fechaHoraInicio;
    private Integer duracionMinutos;
    private EstadoReserva estado;
    private CanchaDTO cancha;

    private AdministradorDTO administrador;

    public ReservaDTO() {}

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}

    public String getApellido() {return apellido;}
    public void setApellido (String apellido) {this.apellido = apellido;}

    public String getTelefono () {return telefono;}
    public void setTelefono (String telefono) {this.telefono = telefono;}

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


    public AdministradorDTO getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorDTO administrador) {
        this.administrador = administrador;
    }

    
}
