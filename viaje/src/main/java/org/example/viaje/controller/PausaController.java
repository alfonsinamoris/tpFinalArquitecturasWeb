package org.example.viaje.controller;

import org.example.viaje.entity.Pausa;
import org.example.viaje.service.PausaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/pausas")
public class PausaController {

    PausaService pausaService;

    public PausaController(PausaService pausaService) {
        this.pausaService = pausaService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Pausa> iniciarPausa(@RequestBody Pausa pausa){
        Pausa pausaNew = pausaService.save(pausa);
        return ResponseEntity.ok(pausaNew);
    }

    @PutMapping("/finaliza/{id}")
    public ResponseEntity<Pausa> finalizarPausa(@PathVariable String id, @RequestBody Pausa pausaConFin){
        Pausa pausa = pausaService.findById(id);
        if(pausa == null){
            return ResponseEntity.notFound().build();
        }
        pausa.setFin(pausaConFin.getFin());
        Pausa pausaUpdate = pausaService.save(pausa);
        return ResponseEntity.ok(pausaUpdate);
    }

    @GetMapping("/byViaje/{idViaje}")
    public ResponseEntity<List<Pausa>> getPausasByViajeId(@PathVariable("idViaje") String idViaje) {
        List<Pausa> pausas = pausaService.findByViajeId(idViaje);
        return ResponseEntity.ok(pausas);
    }

    @GetMapping("/tiempo-total")
    public ResponseEntity<Long> getTiempoTotalPausaSegundos(@RequestParam("idViaje") String idViaje) {
        Long tiempoSegundos = pausaService.calcularTiempoTotalPausaSegundos(idViaje);
        return ResponseEntity.ok(tiempoSegundos);
    }
}
