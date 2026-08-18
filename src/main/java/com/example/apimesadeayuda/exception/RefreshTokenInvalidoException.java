package com.example.apimesadeayuda.exception;

public class RefreshTokenInvalidoException extends RuntimeException {

    public RefreshTokenInvalidoException(String mensaje) {
        super(mensaje);
    }
}