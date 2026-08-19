package com.example.apimesadeayuda.repository;

import com.example.apimesadeayuda.model.Ticket;
import com.example.apimesadeayuda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime; // <-- CAMBIADO de LocalDateTime a Instant
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreadoPor(Usuario usuario);

    @Query("SELECT t FROM Ticket t WHERE t.slaVenceEn < :fechaActual AND t.estado != 'RESUELTO'")
    List<Ticket> findTicketsVencidos(@Param("fechaActual") LocalDateTime fechaActual); // <-- CAMBIADO
}