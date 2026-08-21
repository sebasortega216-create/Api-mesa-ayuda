package com.example.apimesadeayuda.config;

import com.example.apimesadeayuda.security.JwtAuthenticationEntryPoint;
import com.example.apimesadeayuda.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String[] RUTAS_PUBLICAS = {
            "/api/auth/registro",
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/ping",
            "/h2-console/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RUTAS_PUBLICAS).permitAll()

                        // Rutas para SOPORTE y ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/tickets").hasAnyRole("SOPORTE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/tickets/vencidos").hasAnyRole("SOPORTE", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/tickets/{id}/estado").hasAnyRole("SOPORTE", "ADMIN") // ←
                                                                                                                      // CORREGIDO

                        // Rutas para cualquier usuario autenticado
                        .requestMatchers(HttpMethod.POST, "/api/tickets").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tickets/mios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tickets/{id}").authenticated()
                        .requestMatchers("/api/auth/logout").authenticated()

                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}