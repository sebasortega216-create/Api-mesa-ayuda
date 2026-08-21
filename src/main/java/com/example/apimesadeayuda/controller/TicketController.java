package com.example.apimesadeayuda.controller;

import com.example.apimesadeayuda.dto.TicketRequest;
import com.example.apimesadeayuda.dto.TicketResponse;
import com.example.apimesadeayuda.model.Estado;
import com.example.apimesadeayuda.model.Usuario;
import com.example.apimesadeayuda.service.TicketService;
import com.example.apimesadeayuda.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UsuarioService usuarioService; // ← AGREGADO

    @PostMapping
    public ResponseEntity<TicketResponse> crearTicket(
            @Valid @RequestBody TicketRequest request,
            Authentication authentication) {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        TicketResponse response = ticketService.crearTicket(request, usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mios")
    public ResponseEntity<List<TicketResponse>> getMisTickets(Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(ticketService.getTicketsByUsuario(usuario.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable Long id,
            Authentication authentication) {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        TicketResponse response = ticketService.getTicketById(id, usuario.getId(), usuario.getRol());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Estado estado) {

        TicketResponse response = ticketService.cambiarEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<TicketResponse>> getTicketsVencidos() {
        return ResponseEntity.ok(ticketService.getTicketsVencidos());
    }
}