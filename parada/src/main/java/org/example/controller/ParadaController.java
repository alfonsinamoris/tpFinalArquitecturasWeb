package org.example.controller;

import org.example.dto.MonopatinDTO;
import org.example.entity.Parada;
import org.example.service.ParadaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paradas")
public class ParadaController {
    ParadaService paradaService;

    public ParadaController(ParadaService paradaService) {
        this.paradaService = paradaService;
    }

    @GetMapping({"","/"})
    public ResponseEntity<List<Parada>> getAllParadas() {
        List<Parada> paradas = paradaService.getAll();
        if (paradas.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(paradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parada> getParadaById(@PathVariable("id") String id) {
        Parada parada = paradaService.findById(id);
        if (parada == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parada);
    }

    @PostMapping("")
    public ResponseEntity<Parada> save(@RequestBody Parada parada) {
        Parada paradaNew = paradaService.save(parada);
        return ResponseEntity.ok(paradaNew);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParada(@PathVariable("id") String id) {
        boolean eliminado = paradaService.deleteById(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content para eliminación exitosa
        }
        return ResponseEntity.notFound().build(); // 404 Not Found si no existe
    }

    /**
     * Endpoint para registrar un monopatín en parada.
     * @param idParada ID de la parada.
     * @param idMonopatin ID del monopatín a ubicar.
     * @return Respuesta de éxito.
     */
    @PostMapping("/{idParada}/ubicar/{idMonopatin}")
    public ResponseEntity<String> ubicarMonopatinEnParada(@PathVariable("idParada") String idParada, @PathVariable("idMonopatin") String idMonopatin) {

        try {
            MonopatinDTO monopatinActualizado = paradaService.ubicarMonopatin(idParada, idMonopatin);

            return ResponseEntity.ok("Monopatín " + monopatinActualizado.getId() +
                    " ubicado y disponible en parada " + idParada);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Parada no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
