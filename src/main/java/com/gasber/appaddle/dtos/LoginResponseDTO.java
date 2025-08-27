package com.gasber.appaddle.dtos;

public class LoginResponseDTO {
    private String usuario;
    private String token;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }
//getters y setters!
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
