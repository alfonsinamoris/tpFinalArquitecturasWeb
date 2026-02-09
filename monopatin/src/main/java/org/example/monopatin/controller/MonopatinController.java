package org.example.monopatin.controller;

import org.example.monopatin.entity.Monopatin;
import org.example.monopatin.service.MonopatinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

    MonopatinService monopatinService;

    public MonopatinController(MonopatinService monopatinService) {
        this.monopatinService = monopatinService;
    }

    @GetMapping({ "","/"})
    public ResponseEntity<List<Monopatin>> getAllMonopatines() {
        List<Monopatin> monopatines = monopatinService.getAll();
        if (monopatines.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> getMonopatinById(@PathVariable("id") String id) {
        Monopatin monopatin = monopatinService.findById(id);
        if (monopatin == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(monopatin);
    }

    @PostMapping("")
    public ResponseEntity<Monopatin> save(@RequestBody Monopatin monopatin) {
        Monopatin monopatinNew = monopatinService.save(monopatin);
        return ResponseEntity.ok(monopatinNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monopatin> update(@PathVariable("id") String id, @RequestBody Monopatin monopatin) {
        if (!id.equals(monopatin.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Monopatin monopatinUpdate = monopatinService.update(monopatin);
        if (monopatinUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(monopatinUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonopatin(@PathVariable("id") String id) {
        boolean eliminado = monopatinService.deleteById(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content para eliminación exitosa
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/estado/{estado}")
    public ResponseEntity<Monopatin> updateEstadoMonopatin(@PathVariable("id") String id, @PathVariable("estado") String estado) {
        Monopatin monopatinActualizado = monopatinService.updateEstado(id, estado);
        if (monopatinActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(monopatinActualizado);
    }

    @PutMapping("/{id}/evaluar-mantenimiento")
    public ResponseEntity<String> evaluarMantenimiento(@PathVariable("id") String id) {
        try {
            monopatinService.evaluarMantenimiento(id);
            return ResponseEntity.ok("Evaluación de mantenimiento para monopatín " + id + " completada.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/en-paradas")
    public ResponseEntity<List<Monopatin>> getMonopatinesEnParadas(@RequestBody List<String> idParadas) {
        List<Monopatin> monopatines = monopatinService.findMonopatinesEnParadas(idParadas);
        if (monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }


}
