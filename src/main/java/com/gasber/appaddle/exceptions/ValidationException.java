package com.gasber.appaddle.exceptions;

public class ValidationException extends RuntimeException {

    private String campo;
    public ValidationException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
    
}
