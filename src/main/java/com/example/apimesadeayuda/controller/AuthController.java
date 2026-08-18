package com.example.apimesadeayuda.controller;

import com.example.apimesadeayuda.dto.AuthResponse;
import com.example.apimesadeayuda.dto.LoginRequest;
import com.example.apimesadeayuda.dto.RefreshRequest;
import com.example.apimesadeayuda.dto.RegistroRequest;
import com.example.apimesadeayuda.dto.UsuarioResponse;
import com.example.apimesadeayuda.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody RegistroRequest request) {
        UsuarioResponse usuario = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@Valid @RequestBody RefreshRequest request) {
        String nuevoAccessToken = authService.refrescarToken(request.getRefreshToken());
        return ResponseEntity.ok(Map.of(
                "accessToken", nuevoAccessToken,
                "tipo", "Bearer"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request, Authentication authentication) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}