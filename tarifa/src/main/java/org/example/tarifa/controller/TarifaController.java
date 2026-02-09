package org.example.tarifa.controller;


import org.example.tarifa.entity.Tarifa;
import org.example.tarifa.service.TarifaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Tarifa>> getAllTarifas() {
        List<Tarifa> tarifas = tarifaService.getAll();
        if (tarifas.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> getTarifasById(@PathVariable("id") String id) {
        Tarifa tarifa = tarifaService.findById(id);
        if (tarifa == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarifa);
    }

    @PostMapping("")
    public ResponseEntity<Tarifa> save(@RequestBody Tarifa tarifa) {
        Tarifa tarifaNew = tarifaService.save(tarifa);
        return ResponseEntity.ok(tarifaNew);
    }

    @GetMapping("/vigente")
    public ResponseEntity<Tarifa> getTarifaVigente(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaConsulta) {
        Tarifa tarifaVigente = tarifaService.findVigenteByDate(fechaConsulta);

        if (tarifaVigente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarifaVigente);
    }

    @PostMapping("/ajuste")
    public ResponseEntity<Tarifa> registrarAjusteTarifas(   @RequestBody Tarifa tarifa,
                                                            @RequestParam("fechaActivacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaActivacion) {
        Tarifa nuevaTarifa = tarifaService.registrarNuevaTarifa(tarifa, fechaActivacion);
        return ResponseEntity.status(201).body(nuevaTarifa);
    }

}
