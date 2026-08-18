package com.example.apimesadeayuda.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tipo = "Bearer";
    private UsuarioResponse usuario;

    public AuthResponse(String accessToken, String refreshToken, UsuarioResponse usuario) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tipo = "Bearer";
        this.usuario = usuario;
    }
}