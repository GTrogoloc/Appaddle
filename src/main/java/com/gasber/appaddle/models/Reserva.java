package com.gasber.appaddle.models;

import java.time.LocalDateTime;

import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    @Column(nullable = false)
    private LocalDateTime fechaHoraInicio;
    @Column(nullable = false)
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.RESERVADA;

    @Column (name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;
     
    // DATOS DE PAGO
@Column(name = "precio_total")
private Double precioTotal;

@Column(name = "senia_total")
private Double seniaTotal;

@Enumerated(EnumType.STRING)
@Column(name = "estado_pago")
private EstadoPago estadoPago = EstadoPago.PENDIENTE;

@Column(name = "fecha_creacion")
private LocalDateTime fechaCreacion;

@Column(name = "fecha_limite_pago")
private LocalDateTime fechaLimitePago;


    public Reserva() {}

    public Reserva(String nombre, String apellido, String telefono, Cancha cancha, LocalDateTime fechaHoraInicio, EstadoReserva estado) {
       
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.cancha = cancha;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraInicio.plusMinutes(90); //  calculado fijo
        this.estado = estado;
    }

    @PrePersist
    @PreUpdate
    private void calcularFin() {
        if (this.fechaHoraInicio != null) {
            this.fechaHoraFin = this.fechaHoraInicio.plusMinutes(90);
        }
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

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

  //GETTERS Y SETTERS DE PAGOS
  public Double getPrecioTotal() {
    return precioTotal;
}

public void setPrecioTotal(Double precioTotal) {
    this.precioTotal = precioTotal;
}

public Double getSeniaTotal() {
    return seniaTotal;
}

public void setSeniaTotal(Double seniaTotal) {
    this.seniaTotal = seniaTotal;
}

public EstadoPago getEstadoPago() {
    return estadoPago;
}

public void setEstadoPago(EstadoPago estadoPago) {
    this.estadoPago = estadoPago;
}

public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
}

public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
}

public LocalDateTime getFechaLimitePago() {
    return fechaLimitePago;
}

public void setFechaLimitePago(LocalDateTime fechaLimitePago) {
    this.fechaLimitePago = fechaLimitePago;
}

    
}
