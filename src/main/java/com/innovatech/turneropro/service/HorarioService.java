package com.innovatech.turneropro.service;

import com.innovatech.turneropro.dto.HorarioRequest;
import com.innovatech.turneropro.model.HorarioBarbero;
import com.innovatech.turneropro.model.Reserva;
import com.innovatech.turneropro.model.Servicio;
import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.HorarioBarberoRepository;
import com.innovatech.turneropro.repository.ReservaRepository;
import com.innovatech.turneropro.repository.ServicioRepository;
import com.innovatech.turneropro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService {
    
    @Autowired
    private HorarioBarberoRepository horarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    // Tiempo de descanso entre turnos en minutos
    private static final int TIEMPO_DESCANSO_MINUTOS = 10;
    
    public HorarioBarbero crearHorario(HorarioRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario barbero = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (barbero.getRol() != Usuario.RolUsuario.BARBERO) {
            throw new RuntimeException("Solo los barberos pueden configurar horarios");
        }
        
        HorarioBarbero horario = new HorarioBarbero();
        horario.setBarbero(barbero);
        horario.setDiaSemana(DayOfWeek.valueOf(request.getDiaSemana().toUpperCase()));
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setActivo(true);
        
        return horarioRepository.save(horario);
    }
    
    public List<HorarioBarbero> obtenerHorariosBarbero(Long barberoId) {
        Usuario barbero = usuarioRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        return horarioRepository.findByBarberoAndActivoTrue(barbero);
    }
    
    public List<HorarioBarbero> obtenerMisHorarios() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario barbero = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return horarioRepository.findByBarberoAndActivoTrue(barbero);
    }
    
    public void eliminarHorario(Long horarioId) {
        HorarioBarbero horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!horario.getBarbero().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para eliminar este horario");
        }
        
        horario.setActivo(false);
        horarioRepository.save(horario);
    }
    
    /**
     * Obtiene los horarios disponibles para un barbero en una fecha específica
     * considerando la duración del servicio y tiempo de descanso
     * Filtra horarios pasados si la fecha es hoy
     */
    public List<LocalTime> obtenerHorariosDisponiblesPorDia(Long barberoId, LocalDate fecha, Long servicioId) {
        // Obtener barbero
        Usuario barbero = usuarioRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        
        // Obtener servicio para saber la duración
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        // Obtener día de la semana
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        
        // Buscar horario del barbero para ese día
        List<HorarioBarbero> horariosDelDia = horarioRepository
                .findByBarberoAndDiaSemanaAndActivoTrue(barbero, diaSemana);
        
        if (horariosDelDia.isEmpty()) {
            throw new RuntimeException("El barbero no trabaja ese día");
        }
        
        // Obtener hora actual para filtrar horarios pasados
        LocalDateTime ahora = LocalDateTime.now();
        boolean esHoy = fecha.equals(LocalDate.now());
        
        List<LocalTime> horariosDisponibles = new ArrayList<>();
        
        for (HorarioBarbero horarioBarbero : horariosDelDia) {
            LocalTime horaActual = horarioBarbero.getHoraInicio();
            LocalTime horaFin = horarioBarbero.getHoraFin();
            
            // Generar bloques de tiempo
            while (horaActual.plusMinutes(servicio.getDuracionMinutos()).isBefore(horaFin) 
                    || horaActual.plusMinutes(servicio.getDuracionMinutos()).equals(horaFin)) {
                
                LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, horaActual);
                LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(servicio.getDuracionMinutos());
                
                // Si es hoy, solo mostrar horarios futuros (con 30 min de margen mínimo)
                // Si es día futuro, mostrar todos los horarios
                if (esHoy) {
                    if (fechaHoraInicio.isBefore(ahora.plusMinutes(30))) {
                        // Saltar este horario, ya pasó
                        horaActual = horaActual.plusMinutes(servicio.getDuracionMinutos() + TIEMPO_DESCANSO_MINUTOS);
                        continue;
                    }
                }
                
                // Verificar si está ocupado (solo para este barbero específico)
                boolean estaOcupado = reservaRepository.findReservasSolapadas(
                        barbero, fechaHoraInicio, fechaHoraFin).size() > 0;
                
                if (!estaOcupado) {
                    horariosDisponibles.add(horaActual);
                }
                
                // Siguiente bloque: duración del servicio + tiempo de descanso
                horaActual = horaActual.plusMinutes(servicio.getDuracionMinutos() + TIEMPO_DESCANSO_MINUTOS);
            }
        }
        
        return horariosDisponibles;
    }
}
