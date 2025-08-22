package com.gasber.appaddle.models;

import java.time.LocalDateTime;

import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //DATOS DE LA PERSONA QUE RESERVA
    @Column (nullable = false, length = 60)
    private String nombre;

    @Column (length = 60)
    private String apellido;

    @Column (nullable = false, length = 30)
    private String telefono;

    //DATOS DE CANCHA
    @ManyToOne
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;
   
    //DATOS DE ADMIN
    @ManyToOne
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;

    //DATOS DE RESERVA
    private LocalDateTime fechaHoraInicio;

    private int duracionMinutos; // por ejemplo: 60, 90, etc.

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.RESERVADA;

    public Reserva() {}

    public Reserva(String nombre, String apellido, String telefono, Cancha cancha, LocalDateTime fechaHoraInicio, int duracionMinutos, EstadoReserva estado) {
       
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.cancha = cancha;
        this.fechaHoraInicio = fechaHoraInicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}


    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
    
}
