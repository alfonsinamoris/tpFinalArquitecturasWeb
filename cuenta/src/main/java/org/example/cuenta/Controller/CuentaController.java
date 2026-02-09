package org.example.cuenta.Controller;

import org.example.cuenta.entity.Cuenta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.cuenta.Service.CuentaService;

import java.util.List;


@RestController
@RequestMapping("/cuenta")

public class CuentaController {
    CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("")
    public ResponseEntity<List<Cuenta>> getCuentas() {
        List<Cuenta> cuentas = cuentaService.getAll();
        if (cuentas.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable("id") String id) {
        Cuenta cuenta = cuentaService.findById(id);
        if (cuenta == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Cuenta>> getCuentasByUserId(@PathVariable("userId") String userId) {
        List<Cuenta> cuentas = cuentaService.getCuentasByUserId(userId);
        if (cuentas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cuentas);
    }

    @PostMapping("")
    public ResponseEntity<Cuenta> save(@RequestBody Cuenta cuenta) {
        Cuenta nuevaCuenta = cuentaService.save(cuenta);
        return ResponseEntity.ok(nuevaCuenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> update(@PathVariable("id") String id, @RequestBody Cuenta cuenta) {
        Cuenta cuentaUpdate = cuentaService.update(id, cuenta);
        if (cuentaUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuentaUpdate);
    }

    //Recibe x parametro token pago para Mock de mercado pago
    @PostMapping("/cargarSaldo/{id}")
    public ResponseEntity<Cuenta> cargarSaldo(@PathVariable ("id") String id, @RequestParam("saldo") double saldo, @RequestParam("tokenPago") String tokenPago) {
        try {
         Cuenta cuentaActualizada = cuentaService.cargarSaldo(id, saldo, tokenPago);
            if (cuentaActualizada == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            // Maneja el error lanzado por el Service/Mock (pago rechazado)
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/anular/{id}")
    public ResponseEntity<Cuenta> anular(@PathVariable("id") String id) {
        Cuenta cuentaActualizada = cuentaService.anularCuenta(id);
        if (cuentaActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuentaActualizada);
    }
    //h) opcionalmente si
    //otros usuarios relacionados a mi cuenta los han usado.
    @GetMapping("/{id}/usuarios")
    public ResponseEntity<List<String>> getUsuariosAsociados(@PathVariable("id") String id) {
        try {
            List<String> usuarios = cuentaService.getUsuariosAsociados(id);
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }


    }


}

