package com.gasber.appaddle.dtos;

public class LoginResponseDTO {
    private String token;
    private AdministradorDTO administrador;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, AdministradorDTO administrador) {
        this.token = token;
        this.administrador = administrador;
    }
//getters y setters!
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdministradorDTO getAdministrador() {
        return administrador;
    }
    
    public void setAdministrador(AdministradorDTO administrador) {
        this.administrador = administrador;
    }

}
