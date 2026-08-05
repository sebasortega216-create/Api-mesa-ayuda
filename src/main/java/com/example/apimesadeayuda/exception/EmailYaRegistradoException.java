package com.example.apimesadeayuda.exception;

public class EmailYaRegistradoException extends RuntimeException {

    public EmailYaRegistradoException(String email) {
        super("El email ya está registrado: " + email);
    }
}