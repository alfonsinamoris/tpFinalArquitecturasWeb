package org.example.usuario.controller;
import org.example.usuario.dto.monopatinDto;
import org.example.usuario.dto.reporteUsoDto;
import org.example.usuario.service.UsuarioService;
import org.example.usuario.entity.Usuario;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class usuarioController {

    private final UsuarioService usuarioService;

    public usuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable String id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.save(usuario));
    }

    @GetMapping("/cercanos")
    public ResponseEntity<List<monopatinDto>> getMonopatinesCercanos(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiokm) {

        List<monopatinDto> monopatines = usuarioService.buscarMonopatinesCercanos(lat, lng, radiokm);

        if (monopatines == null || monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(monopatines);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}/reporteUso")
    public ResponseEntity<reporteUsoDto> getReporteUso(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam boolean otrosUsuarios) {

        return ResponseEntity.ok(usuarioService.getReporteUso(userId, fechaInicio, fechaFin, otrosUsuarios));
    }

    @GetMapping("/auth-data")
    public ResponseEntity<Usuario> getUsuarioParaAutenticacion(@RequestParam("username") String username) {
        Usuario usuario = usuarioService.getUsuarioParaAutenticacion(username);
        return (usuario == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuario);
    }




}

