package org.example.administrador.controller;
import org.example.administrador.dto.*;
import org.example.administrador.feingClients.FacturacionFeingClient;
import org.example.administrador.service.AdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdminController {

    AdminService adminService;
    FacturacionFeingClient facturacionFeingClient;

    public AdminController(AdminService adminService, FacturacionFeingClient facturacionFeingClient) {
        this.adminService = adminService;
        this.facturacionFeingClient = facturacionFeingClient;
    }
    /**
     * PUNTO A
     * Genera el reporte de uso de monopatines por kilómetros/tiempo.
     * URL: GET http://localhost:8080/administrador/reportes/mantenimiento-uso
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reportes/mantenimiento-uso")
    public ResponseEntity<List<ReporteMonopatinXKm>> getReporteMonopatines(@RequestParam("conPausas") boolean conPausas) {
        List<ReporteMonopatinXKm> reportes = adminService.generarReporteUso(conPausas);
        if (reportes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reportes);
    }

    /**
     * PUNTO B
     * Anula una cuenta de usuario.
     * URL: PUT http://localhost:8080/administrador/cuentas/anular/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cuentas/anular/{idCuenta}")
    public ResponseEntity<String> anularCuenta(@PathVariable("idCuenta") String idCuenta) {
        try {
            adminService.anularCuentaUsuario(idCuenta);
            return ResponseEntity.ok("Cuenta anulada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * PUNTO C
     * Consulta los monopatines con más de X viajes en un cierto año.
     * URL: GET /administrador/reportes/top-monopatines?minViajes={X}&year={Año}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reportes/top-monopatines")
    public ResponseEntity<List<ReporteMonopatinContadorViajes>> getMonopatinesConMasDeXViajes(
                                                            @RequestParam("minViajes") int minViajes,
                                                            @RequestParam("year") int year) {

        List<ReporteMonopatinContadorViajes> topMonopatines = adminService.getMonopatinesConMasDeXViajes(minViajes, year);

        if (topMonopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topMonopatines);
    }

    /**
     * PUNTO D
     * Consulta el total facturado en un rango de meses en un cierto año-
     * URL: GET /administrador/total-facturado?anio={X}&mesInicio={X}&mesFin={X}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total-facturado")
    public ResponseEntity<Double> obtenerTotalFacturado(
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin) {

        Double total = adminService.obtenerTotalFacturado(anio, mesInicio, mesFin);
        return ResponseEntity.ok(total);
    }

    /**
     * PUNTO E
     * Consulta los usuarios que mas usan monopatines filtrados por rol y periodo
     * URL: GET /api/admin/usuarios-que-mas-usan?rol={X}&inicio={X}&fin={X}
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios-que-mas-usan")
    public ResponseEntity<List<UsuarioUsoDTO>> getUsuariosQueMasUsanMonopatines( @RequestParam String rol,
                                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        List<UsuarioUsoDTO> reporte = adminService.getUsuariosQueMasUsanMonopatines(rol, inicio, fin);

        if (reporte == null || reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }

    /**
     * PUNTO F
     * Consulta los usuarios que mas usan monopatines filtrados por rol y periodo
     * URL: GET /api/admin/ajuste?fechaActivacion={X}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ajuste")
    public ResponseEntity<String> ajustarTarifas(   @RequestBody TarifaDTO tarifaDTO,
                                                    @RequestParam("fechaActivacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaActivacion) {
        adminService.ajustarTarifas(tarifaDTO, fechaActivacion);
        return ResponseEntity.ok().build();
    }


}
