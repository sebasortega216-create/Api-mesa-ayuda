package com.example.apimesadeayuda.repository;

import com.example.apimesadeayuda.model.RefreshToken;
import com.example.apimesadeayuda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsuario(Usuario usuario);
}