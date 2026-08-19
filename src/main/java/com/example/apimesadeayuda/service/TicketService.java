package com.example.apimesadeayuda.service;

import com.example.apimesadeayuda.dto.TicketRequest;
import com.example.apimesadeayuda.dto.TicketResponse;
import com.example.apimesadeayuda.dto.UsuarioResponse;
import com.example.apimesadeayuda.exception.RecursoNoEncontradoException;
import com.example.apimesadeayuda.exception.AccesoDenegadoException;
import com.example.apimesadeayuda.model.Estado;
import com.example.apimesadeayuda.model.Prioridad;
import com.example.apimesadeayuda.model.Rol;
import com.example.apimesadeayuda.model.Ticket;
import com.example.apimesadeayuda.model.Usuario;
import com.example.apimesadeayuda.repository.TicketRepository;
import com.example.apimesadeayuda.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime; // ← SOLO LocalDateTime
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    private static final int SLA_ALTA_HORAS = 4;
    private static final int SLA_MEDIA_HORAS = 24;
    private static final int SLA_BAJA_HORAS = 72;

    @Transactional
    public TicketResponse crearTicket(TicketRequest request, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        LocalDateTime ahora = LocalDateTime.now(); // ← CORREGIDO

        int horasSLA = switch (request.getPrioridad()) {
            case ALTA -> SLA_ALTA_HORAS;
            case MEDIA -> SLA_MEDIA_HORAS;
            case BAJA -> SLA_BAJA_HORAS;
        };

        Ticket ticket = Ticket.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .prioridad(request.getPrioridad())
                .estado(Estado.ABIERTO) // ← CORREGIDO
                .creadoEn(ahora)
                .slaVenceEn(ahora.plusHours(horasSLA)) // ← CORREGIDO
                .creadoPor(usuario)
                .build();

        Ticket guardado = ticketRepository.save(ticket);
        return convertirAResponse(guardado);
    }

    public List<TicketResponse> getTicketsByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        return ticketRepository.findByCreadoPor(usuario).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long ticketId, Long usuarioId, Rol rol) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        if (rol == Rol.USUARIO && !ticket.getCreadoPor().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException("No tienes permiso para ver este ticket");
        }

        return convertirAResponse(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse cambiarEstado(Long ticketId, Estado nuevoEstado) { // ← CORREGIDO: Estado, no EstadoTicket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        ticket.setEstado(nuevoEstado);
        Ticket actualizado = ticketRepository.save(ticket);
        return convertirAResponse(actualizado);
    }

    public List<TicketResponse> getTicketsVencidos() {
        LocalDateTime ahora = LocalDateTime.now(); // ← CORREGIDO: LocalDateTime, no Instant
        return ticketRepository.findTicketsVencidos(ahora).stream()
                .map(this::convertirAResponse) // ← CORREGIDO: convertirAResponse
                .collect(Collectors.toList());
    }

    private TicketResponse convertirAResponse(Ticket ticket) {
        LocalDateTime slaVence = ticket.getSlaVenceEn();
        LocalDateTime ahora = LocalDateTime.now();

        boolean vencido = slaVence.isBefore(ahora)
                && ticket.getEstado() != Estado.RESUELTO; // ← CORREGIDO

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getPrioridad(),
                ticket.getEstado(),
                ticket.getCreadoEn(),
                slaVence,
                UsuarioResponse.desde(ticket.getCreadoPor()),
                vencido);
    }
}