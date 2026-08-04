package com.example.apimesadeayuda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tipo = "Bearer";
    private UsuarioResponse usuario;

    public AuthResponse(String accessToken, UsuarioResponse usuario) {
        this.accessToken = accessToken;
        this.tipo = "Bearer";
        this.usuario = usuario;
    }
}