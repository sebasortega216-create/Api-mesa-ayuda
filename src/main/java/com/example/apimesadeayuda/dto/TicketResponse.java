package com.example.apimesadeayuda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.apimesadeayuda.model.Prioridad;
import com.example.apimesadeayuda.model.Estado;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;
    private LocalDateTime creadoEn;
    private LocalDateTime slaVenceEn;
    private UsuarioResponse creadoPor;
    private boolean vencido;
}