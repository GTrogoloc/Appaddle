package com.gasber.appaddle.dtos;

public class AdministradorDTO {
    private Long id;
    private String usuario;

    public AdministradorDTO() {}    

    public AdministradorDTO(Long id, String usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
}
