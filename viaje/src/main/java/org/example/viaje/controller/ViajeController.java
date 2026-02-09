package org.example.viaje.controller;

import org.example.viaje.dto.ReporteMonopatinContadorViajes;
import org.example.viaje.dto.ReporteUsoDTO;
import org.example.viaje.dto.UsuarioUsoDTO;
import org.example.viaje.entity.Viaje;
import org.example.viaje.service.ViajeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/viajes")
public class ViajeController {

    ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Viaje>> getAllViajes() {
        List<Viaje> viajes = viajeService.getAll();
        if (viajes.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(viajes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> getViajeById(@PathVariable("id") String id) {
        Viaje viaje = viajeService.findById(id);
        if (viaje == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(viaje);
    }

    /**
     * Endpoint consultado por Administrador/Monopatín para obtener viajes de un Monopatín.
     * URL: GET http://localhost:8084/viajes/byMonopatin/{monopatinId}
     */
    @GetMapping("/byMonopatin/{monopatinId}")
    public ResponseEntity<List<Viaje>> getViajesByMonopatinId(@PathVariable("monopatinId") String monopatinId) {
        List<Viaje> viajes = viajeService.byMonopatinId(monopatinId);
        if (viajes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(viajes);
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Viaje>> getViajesByUserId(@PathVariable("userId") String userId) {
        List<Viaje> viajes = viajeService.byUserId(userId);
        return ResponseEntity.ok(viajes);
    }

    /**
     * Inicia un viaje. Llama al Monopatín para ponerlo en uso.
     * URL: POST http://localhost:8083/viajes/iniciar
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/iniciar")
    public ResponseEntity<Viaje> iniciarViaje(@RequestBody Viaje viaje) {
        try {
            Viaje viajeNuevo = viajeService.iniciarViaje(viaje);
            return ResponseEntity.status(201).body(viajeNuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .body(null);
        }
    }

    /**
     * Finaliza un viaje. Llama a Parada para validar ubicación, actualiza Monopatín y notifica a Facturación.
     * URL: PUT http://localhost:8083/viajes/finalizar/{id}
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Viaje> finalizarViaje(
            @PathVariable("id") String idViaje,
            @RequestParam("idParadaFin") String idParadaFin,
            @RequestParam("kmRecorridos") float kmRecorridosFinal) {

        try {
            Viaje viajeFinalizado = viajeService.finalizarViaje(idViaje, idParadaFin, kmRecorridosFinal);
            return ResponseEntity.ok(viajeFinalizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", e.getMessage())
                    .body(null);
        }
    }

    /**
     * Endpoint consultado por Administrador para generar el reporte de monopatines más usados.
     * URL: GET /viajes/reportes/monopatines-por-viajes?year={year}
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/reportes/monopatines-por-viajes")
    public ResponseEntity<List<ReporteMonopatinContadorViajes>> getMonopatinesPorViajes(@RequestParam("year") int year) {

        List<ReporteMonopatinContadorViajes> reporte = viajeService.getConteoViajesPorMonopatinYAnio(year);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/reporte-uso")
    public ReporteUsoDTO getReporteUso(
            @RequestParam(name = "userIds") List<String> userIds,

            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return viajeService.getReporteUsoDto(userIds, fechaInicio, fechaFin);
    }

    /**
     * Endpoint para el Punto E
     * URL: GET /viajes/reportes/usuarios-uso?inicio={X}&fin={X}&tipoUsuario={X}
     */
    @GetMapping("/reportes/usuarios-uso")
    public ResponseEntity<List<UsuarioUsoDTO>> obtenerUsuariosMasActivos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam("userIds") String userIdsCsv) {

        List<String> userIds = Arrays.asList(userIdsCsv.split(","));

        List<UsuarioUsoDTO> resultado = viajeService.obtenerUsuariosMasActivos(inicio, fin, userIds);

        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resultado);
    }
}
