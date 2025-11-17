package com.innovatech.turneropro.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Servicio singleton para envío de correos electrónicos usando Jakarta Mail API
 * Configurado específicamente para Gmail SMTP con App Password
 */
public class ServicioCorreoSingleton {

    private static ServicioCorreoSingleton instancia;

    // ⚠️ Configuración de cuenta Gmail para TurneroPro
    // App Password generado el 17/11/2025 a las 3:34 PM
    private final String remitente = "turneropro2025@gmail.com";
    private final String clave = "tbeagxwqlhlcgpll";  // App Password de Gmail (sin espacios)

    private final Session sesion;

    private ServicioCorreoSingleton() throws MessagingException {
        this.sesion = crearSesionSMTP();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("✅ ServicioCorreoSingleton inicializado con Jakarta Mail");
        System.out.println("📧 Remitente: " + remitente);
        System.out.println("🔐 Protocolo: Gmail SMTP over TLS (587)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    public static synchronized ServicioCorreoSingleton getInstancia() throws MessagingException {
        if (instancia == null) {
            instancia = new ServicioCorreoSingleton();
        }
        return instancia;
    }

    private Session crearSesionSMTP() {
        Properties props = new Properties();
        
        // Configuración SMTP de Gmail
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        
        // STARTTLS (requerido por Gmail)
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        
        // Configuración SSL/TLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        // Timeouts (evitar bloqueos)
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        
        // Debug activado para diagnóstico
        props.put("mail.debug", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });
    }

    public boolean enviarCorreo(String destinatario, String asunto, String cuerpoHTML) {
        System.out.println("\n📤 Intentando enviar correo...");
        System.out.println("   Destinatario: " + destinatario);
        System.out.println("   Asunto: " + asunto);
        
        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(remitente, "TurneroPro"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setContent(cuerpoHTML, "text/html; charset=utf-8");

            // Enviar mensaje
            Transport.send(mensaje);
            
            System.out.println("✅ ¡Correo enviado exitosamente a: " + destinatario + "!");
            return true;
            
        } catch (MessagingException e) {
            System.err.println("\n❌ ERROR AL ENVIAR CORREO:");
            System.err.println("   Destinatario: " + destinatario);
            System.err.println("   Error: " + e.getMessage());
            e.printStackTrace();
            
            // Diagnóstico específico
            if (e.getMessage().contains("535-5.7.8 Username and Password not accepted")) {
                System.err.println("\n⚠️  DIAGNÓSTICO: Credenciales incorrectas");
                System.err.println("   Verifica que estés usando un App Password de Gmail (16 caracteres)");
                System.err.println("   Genera uno en: https://myaccount.google.com/apppasswords");
            } else if (e.getMessage().contains("Connection timed out")) {
                System.err.println("\n⚠️  DIAGNÓSTICO: Problema de conexión");
                System.err.println("   Verifica la conexión a Internet y el firewall");
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarCorreoTextoPlano(String destinatario, String asunto, String cuerpoTexto) {
        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(remitente, "TurneroPro"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpoTexto);

            Transport.send(mensaje);
            System.out.println("✅ Correo de texto plano enviado a: " + destinatario);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar correo de texto plano a: " + destinatario);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
