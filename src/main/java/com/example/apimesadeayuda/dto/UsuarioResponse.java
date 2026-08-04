package com.example.apimesadeayuda.dto;

import com.example.apimesadeayuda.model.Rol;
import com.example.apimesadeayuda.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private Rol rol;

    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol());
    }
}