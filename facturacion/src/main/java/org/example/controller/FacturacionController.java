package org.example.controller;


import org.example.entity.Facturacion;
import org.example.service.FacturacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturacionController {
    FacturacionService facturacionService;

    public FacturacionController(FacturacionService facturacionService) {
        this.facturacionService = facturacionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Facturacion>> getAllFacturacion() {
        List<Facturacion> facturas = facturacionService.getAll();
        if (facturas.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facturacion> getFacturacionById(@PathVariable("id") String id) {
        Facturacion factura = facturacionService.findById(id);
        if (factura == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(factura);
    }

    @PostMapping("")
    public ResponseEntity<Facturacion> save(@RequestBody Facturacion facturacion) {
        Facturacion facturaNew = facturacionService.save(facturacion);
        return ResponseEntity.ok(facturaNew);
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Facturacion>> getFacturacionByUserId(@PathVariable("userId") String userId) {
        List<Facturacion> facturas = facturacionService.byUserId(userId);
        return ResponseEntity.ok(facturas);
    }


    //URL: GET /facturacion/total-facturado?anio={X}&mesInicio={X}&mesFin={X}
    @GetMapping("/total-facturado")
    public ResponseEntity<Double> getTotalFacturado(
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin) {
        Double total = facturacionService.obtenerTotalFacturado(anio, mesInicio, mesFin);
        return ResponseEntity.ok(total);
    }
}
