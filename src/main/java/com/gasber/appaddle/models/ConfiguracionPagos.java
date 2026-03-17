package com.gasber.appaddle.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;


@Entity
@Table(name = "configuracion_pagos")
public class ConfiguracionPagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio_turno", nullable = false)
    private Double precioTurno;

    @Column(name = "senia_turno", nullable = false)
    private Double seniaTurno;

    @Column(name = "horas_cancelacion", nullable = false)
    private Integer horasCancelacion;

    public ConfiguracionPagos() {}

    public ConfiguracionPagos(Double precioTurno, Double seniaTurno, Integer horasCancelacion) {
        this.precioTurno = precioTurno;
        this.seniaTurno = seniaTurno;
        this.horasCancelacion = horasCancelacion;
    }

    public Long getId() {
        return id;
    }

    public Double getPrecioTurno() {
        return precioTurno;
    }

    public void setPrecioTurno(Double precioTurno) {
        this.precioTurno = precioTurno;
    }

    public Double getSeniaTurno() {
        return seniaTurno;
    }

    public void setSeniaTurno(Double seniaTurno) {
        this.seniaTurno = seniaTurno;
    }

    public Integer getHorasCancelacion() {
        return horasCancelacion;
    }

    public void setHorasCancelacion(Integer horasCancelacion) {
        this.horasCancelacion = horasCancelacion;
    }
    
}
