package com.innovatech.turneropro.service;

import com.innovatech.turneropro.dto.ReservaRequest;
import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.model.Servicio;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.ReservaRepository;
import com.innovatech.turneropro.repository.ServicioRepository;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Reserva crearReserva(ReservaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Usuario barbero = usuarioRepository.findById(request.getBarberoId())
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        // Calcular hora de fin basándose en la duración del servicio
        LocalDateTime fechaHoraFin = request.getFechaHoraInicio()
                .plusMinutes(servicio.getDuracionMinutos());
        
        // Validar que no haya solapamiento
        List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(
                barbero, request.getFechaHoraInicio(), fechaHoraFin);
        
        if (!reservasSolapadas.isEmpty()) {
            throw new RuntimeException("El horario seleccionado no está disponible");
        }
        
        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setBarbero(barbero);
        reserva.setServicio(servicio);
        reserva.setFechaHoraInicio(request.getFechaHoraInicio());
        reserva.setFechaHoraFin(fechaHoraFin);
        reserva.setNotasCliente(request.getNotasCliente());
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reserva.setFechaCreacion(LocalDateTime.now());
        
        Reserva reservaGuardada = reservaRepository.save(reserva);
        
        // Enviar notificación por correo al cliente
        emailService.enviarConfirmacionReserva(reservaGuardada);
        
        // Enviar notificación al barbero de nueva reserva
        emailService.enviarNotificacionNuevaReserva(reservaGuardada);
        
        return reservaGuardada;
    }
    
    public List<Reserva> obtenerMisReservas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (usuario.getRol() == Usuario.RolUsuario.CLIENTE) {
            return reservaRepository.findByCliente(usuario);
        } else if (usuario.getRol() == Usuario.RolUsuario.BARBERO) {
            return reservaRepository.findByBarbero(usuario);
        }
        
        return List.of();
    }
    
    public Reserva cancelarReserva(Long reservaId, String motivo) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reserva.getCliente().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para cancelar esta reserva");
        }
        
        if (reserva.getEstado() == Reserva.EstadoReserva.CANCELADA) {
            throw new RuntimeException("La reserva ya está cancelada");
        }
        
        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
        reserva.setFechaCancelacion(LocalDateTime.now());
        reserva.setMotivoCancelacion(motivo);
        
        Reserva reservaCancelada = reservaRepository.save(reserva);
        
        // Enviar notificación de cancelación
        emailService.enviarCancelacionReserva(reservaCancelada);
        
        return reservaCancelada;
    }
    
    public Reserva reprogramarReserva(Long reservaId, String nuevaFechaHoraStr) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reserva.getCliente().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para reprogramar esta reserva");
        }
        
        if (reserva.getEstado() == Reserva.EstadoReserva.CANCELADA) {
            throw new RuntimeException("No se puede reprogramar una reserva cancelada");
        }
        
        if (reserva.getEstado() == Reserva.EstadoReserva.COMPLETADA) {
            throw new RuntimeException("No se puede reprogramar una reserva completada");
        }
        
        // Parsear nueva fecha
        LocalDateTime nuevaFechaHora = LocalDateTime.parse(nuevaFechaHoraStr);
        LocalDateTime nuevaFechaHoraFin = nuevaFechaHora.plusMinutes(reserva.getServicio().getDuracionMinutos());
        
        // Validar que no haya solapamiento
        List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(
                reserva.getBarbero(), nuevaFechaHora, nuevaFechaHoraFin);
        
        // Filtrar la reserva actual de las solapadas
        reservasSolapadas = reservasSolapadas.stream()
                .filter(r -> !r.getId().equals(reservaId))
                .toList();
        
        if (!reservasSolapadas.isEmpty()) {
            throw new RuntimeException("El nuevo horario seleccionado no está disponible");
        }
        
        // Actualizar fechas
        reserva.setFechaHoraInicio(nuevaFechaHora);
        reserva.setFechaHoraFin(nuevaFechaHoraFin);
        reserva.setRecordatorioEnviado(false); // Resetear recordatorio
        
        Reserva reservaReprogramada = reservaRepository.save(reserva);
        
        // Enviar notificaciones
        emailService.enviarNotificacionReprogramacion(reservaReprogramada);
        
        return reservaReprogramada;
    }
    
    public Reserva completarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reserva.getBarbero().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para completar esta reserva");
        }
        
        if (reserva.getEstado() != Reserva.EstadoReserva.CONFIRMADA) {
            throw new RuntimeException("Solo se pueden completar reservas confirmadas");
        }
        
        reserva.setEstado(Reserva.EstadoReserva.COMPLETADA);
        
        return reservaRepository.save(reserva);
    }
}
