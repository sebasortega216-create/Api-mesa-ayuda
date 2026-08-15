package com.example.apimesadeayuda.service;

import com.example.apimesadeayuda.dto.AuthResponse;
import com.example.apimesadeayuda.dto.LoginRequest;
import com.example.apimesadeayuda.dto.RegistroRequest;
import com.example.apimesadeayuda.dto.UsuarioResponse;
import com.example.apimesadeayuda.exception.CredencialesInvalidasException;
import com.example.apimesadeayuda.exception.EmailYaRegistradoException;
import com.example.apimesadeayuda.model.Rol;
import com.example.apimesadeayuda.model.Usuario;
import com.example.apimesadeayuda.repository.UsuarioRepository;
import com.example.apimesadeayuda.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailYaRegistradoException(request.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.USUARIO)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioResponse.desde(guardado);
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }

        String accessToken = jwtUtil.generarAccessToken(usuario.getEmail(), usuario.getRol().name());

        return new AuthResponse(accessToken, UsuarioResponse.desde(usuario));
    }
}