package com.gasber.appaddle.dtos;

public class ApiResponse <T> {

    private int status;
    private String mensaje;
    private T data;

    public ApiResponse(int status, String mensaje, T data) {
        this.status = status;
        this.mensaje = mensaje;
        this.data = data;
    }

    // Getters y Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}