package com.innovatech.turneropro.service;

import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderScheduler {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Se ejecuta cada hora para verificar si hay citas que necesitan recordatorio
     * Busca reservas confirmadas que:
     * - Están entre 23 y 25 horas en el futuro
     * - No han recibido recordatorio aún
     */
    @Scheduled(cron = "0 0 * * * *") // Cada hora en punto
    public void enviarRecordatorios24Horas() {
        System.out.println("===========================================");
        System.out.println("Ejecutando tarea de recordatorios automáticos...");
        System.out.println("Hora actual: " + LocalDateTime.now());
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentro23Horas = ahora.plusHours(23);
        LocalDateTime dentro25Horas = ahora.plusHours(25);
        
        // Buscar reservas confirmadas sin recordatorio en las próximas 24h
        List<Reserva> reservasPendientes = reservaRepository.findAll().stream()
                .filter(r -> r.getEstado() == Reserva.EstadoReserva.CONFIRMADA)
                .filter(r -> !r.getRecordatorioEnviado())
                .filter(r -> r.getFechaHoraInicio().isAfter(dentro23Horas) 
                          && r.getFechaHoraInicio().isBefore(dentro25Horas))
                .toList();
        
        if (reservasPendientes.isEmpty()) {
            System.out.println("No hay recordatorios para enviar en este momento.");
        } else {
            System.out.println("Enviando " + reservasPendientes.size() + " recordatorio(s)...");
            
            for (Reserva reserva : reservasPendientes) {
                try {
                    emailService.enviarRecordatorio24Horas(reserva);
                    reserva.setRecordatorioEnviado(true);
                    reservaRepository.save(reserva);
                    
                    System.out.println("✓ Recordatorio enviado para reserva #" + reserva.getId() 
                            + " - Cliente: " + reserva.getCliente().getNombreCompleto());
                } catch (Exception e) {
                    System.err.println("✗ Error enviando recordatorio para reserva #" + reserva.getId() 
                            + ": " + e.getMessage());
                }
            }
        }
        
        System.out.println("===========================================");
    }
}
