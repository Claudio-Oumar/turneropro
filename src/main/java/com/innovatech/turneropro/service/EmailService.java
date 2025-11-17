package com.innovatech.turneropro.service;

import com.innovatech.turneropro.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${mail.from}")
    private String fromEmail;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public void enviarConfirmacionReserva(Reserva reserva) {
        try {
            System.out.println("===========================================");
            System.out.println("📧 Intentando enviar email de confirmación...");
            System.out.println("De: " + fromEmail);
            System.out.println("Para: " + reserva.getCliente().getEmail());
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reserva.getCliente().getEmail());
            message.setSubject("Confirmación de Reserva - TurneroPro");
            
            String texto = String.format(
                "Hola %s,\n\n" +
                "Tu reserva ha sido confirmada exitosamente.\n\n" +
                "Detalles de la reserva:\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Duración aproximada: %d minutos\n\n" +
                "Te esperamos!\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getServicio().getDuracionMinutos()
            );
            
            message.setText(texto);
            mailSender.send(message);
            
            System.out.println("✅ Email de confirmación enviado exitosamente a: " + reserva.getCliente().getEmail());
            System.out.println("===========================================");
        } catch (Exception e) {
            System.err.println("===========================================");
            System.err.println("❌ ERROR al enviar email de confirmación");
            System.err.println("Tipo de error: " + e.getClass().getName());
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            System.err.println("===========================================");
        }
    }
    
    public void enviarCancelacionReserva(Reserva reserva) {
        try {
            // Email al cliente
            SimpleMailMessage messageCliente = new SimpleMailMessage();
            messageCliente.setFrom(fromEmail);
            messageCliente.setTo(reserva.getCliente().getEmail());
            messageCliente.setSubject("Cancelación de Reserva - TurneroPro");
            
            String textoCliente = String.format(
                "Hola %s,\n\n" +
                "Tu reserva ha sido cancelada.\n\n" +
                "Detalles de la reserva cancelada:\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Motivo: %s\n\n" +
                "Puedes realizar una nueva reserva cuando lo desees.\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getMotivoCancelacion() != null ? reserva.getMotivoCancelacion() : "No especificado"
            );
            
            messageCliente.setText(textoCliente);
            mailSender.send(messageCliente);
            
            // Email al barbero
            SimpleMailMessage messageBarbero = new SimpleMailMessage();
            messageBarbero.setFrom(fromEmail);
            messageBarbero.setTo(reserva.getBarbero().getEmail());
            messageBarbero.setSubject("Cancelación de Reserva - TurneroPro");
            
            String textoBarbero = String.format(
                "Hola %s,\n\n" +
                "Se ha cancelado una reserva:\n\n" +
                "- Cliente: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getBarbero().getNombreCompleto(),
                reserva.getCliente().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER)
            );
            
            messageBarbero.setText(textoBarbero);
            mailSender.send(messageBarbero);
            
            System.out.println("Emails de cancelación enviados");
        } catch (Exception e) {
            System.err.println("Error al enviar email de cancelación: " + e.getMessage());
        }
    }
    
    public void enviarNotificacionNuevaReserva(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reserva.getBarbero().getEmail());
            message.setSubject("Nueva Reserva - TurneroPro");
            
            String texto = String.format(
                "Hola %s,\n\n" +
                "Tienes una nueva reserva:\n\n" +
                "- Cliente: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Duración: %d minutos\n" +
                "- Teléfono cliente: %s\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getBarbero().getNombreCompleto(),
                reserva.getCliente().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getServicio().getDuracionMinutos(),
                reserva.getCliente().getTelefono() != null ? reserva.getCliente().getTelefono() : "No proporcionado"
            );
            
            message.setText(texto);
            mailSender.send(message);
            
            System.out.println("Email de nueva reserva enviado a barbero: " + reserva.getBarbero().getEmail());
        } catch (Exception e) {
            System.err.println("Error al enviar email de nueva reserva al barbero: " + e.getMessage());
        }
    }
    
    public void enviarNotificacionReprogramacion(Reserva reserva) {
        try {
            // Email al cliente
            SimpleMailMessage messageCliente = new SimpleMailMessage();
            messageCliente.setFrom(fromEmail);
            messageCliente.setTo(reserva.getCliente().getEmail());
            messageCliente.setSubject("Reserva Reprogramada - TurneroPro");
            
            String textoCliente = String.format(
                "Hola %s,\n\n" +
                "Tu reserva ha sido reprogramada exitosamente.\n\n" +
                "Nuevos detalles:\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Nueva fecha y hora: %s\n" +
                "- Duración aproximada: %d minutos\n\n" +
                "Te esperamos!\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getServicio().getDuracionMinutos()
            );
            
            messageCliente.setText(textoCliente);
            mailSender.send(messageCliente);
            
            // Email al barbero
            SimpleMailMessage messageBarbero = new SimpleMailMessage();
            messageBarbero.setFrom(fromEmail);
            messageBarbero.setTo(reserva.getBarbero().getEmail());
            messageBarbero.setSubject("Reserva Reprogramada - TurneroPro");
            
            String textoBarbero = String.format(
                "Hola %s,\n\n" +
                "Se ha reprogramado una reserva:\n\n" +
                "- Cliente: %s\n" +
                "- Servicio: %s\n" +
                "- Nueva fecha y hora: %s\n" +
                "- Teléfono cliente: %s\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getBarbero().getNombreCompleto(),
                reserva.getCliente().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER),
                reserva.getCliente().getTelefono() != null ? reserva.getCliente().getTelefono() : "No proporcionado"
            );
            
            messageBarbero.setText(textoBarbero);
            mailSender.send(messageBarbero);
            
            System.out.println("Emails de reprogramación enviados");
        } catch (Exception e) {
            System.err.println("Error al enviar emails de reprogramación: " + e.getMessage());
        }
    }
    
    public void enviarRecordatorio24Horas(Reserva reserva) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reserva.getCliente().getEmail());
            message.setSubject("Recordatorio de Cita - TurneroPro");
            
            String texto = String.format(
                "Hola %s,\n\n" +
                "Te recordamos que tienes una cita mañana:\n\n" +
                "- Barbero: %s\n" +
                "- Servicio: %s\n" +
                "- Fecha y hora: %s\n" +
                "- Dirección: [Dirección de la barbería]\n\n" +
                "Si necesitas cancelar o reprogramar, puedes hacerlo desde tu panel de cliente.\n\n" +
                "¡Te esperamos!\n\n" +
                "Equipo TurneroPro - Barber Shop Edition",
                reserva.getCliente().getNombreCompleto(),
                reserva.getBarbero().getNombreCompleto(),
                reserva.getServicio().getNombre(),
                reserva.getFechaHoraInicio().format(FORMATTER)
            );
            
            message.setText(texto);
            mailSender.send(message);
            
            System.out.println("Recordatorio de 24h enviado a: " + reserva.getCliente().getEmail());
        } catch (Exception e) {
            System.err.println("Error al enviar recordatorio: " + e.getMessage());
        }
    }
}
