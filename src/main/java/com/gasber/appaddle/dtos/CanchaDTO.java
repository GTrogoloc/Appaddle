package com.gasber.appaddle.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class CanchaDTO {
     @Schema(accessMode = Schema.AccessMode.READ_ONLY)
     private Long id;
     
    private String nombre;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activa;

    public CanchaDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
}
