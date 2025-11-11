package com.innovatech.turneropro.controller;

import com.innovatech.turneropro.model.Usuario;
import com.innovatech.turneropro.repository.UsuarioRepository;
import com.innovatech.turneropro.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barberos")
@CrossOrigin(origins = "*")
public class BarberoController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private HorarioService horarioService;
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Usuario>> obtenerBarberosDisponibles() {
        List<Usuario> barberos = usuarioRepository.findByRol(Usuario.RolUsuario.BARBERO);
        return ResponseEntity.ok(barberos);
    }
    
    @GetMapping("/{barberoId}/horarios-disponibles")
    public ResponseEntity<?> obtenerHorariosDisponibles(
            @PathVariable Long barberoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long servicioId) {
        try {
            List<LocalTime> horariosDisponibles = horarioService.obtenerHorariosDisponiblesPorDia(
                    barberoId, fecha, servicioId);
            return ResponseEntity.ok(Map.of("horarios", horariosDisponibles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }
}
