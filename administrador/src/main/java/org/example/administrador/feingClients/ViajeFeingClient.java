package org.example.administrador.feingClients;
import org.example.administrador.config.FeignClientConfig;
import org.example.administrador.dto.ReporteUsoDTO;
import org.example.administrador.dto.ReporteMonopatinContadorViajes;
import org.example.administrador.dto.UsuarioUsoDTO;
import org.example.administrador.dto.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@FeignClient(name = "microservicio-viaje", url = "http://localhost:8084/viajes", configuration = FeignClientConfig.class)
public interface ViajeFeingClient {
    /**
     * Consulta a Viaje (PausaController) para obtener el tiempo total de pausa.
     */
    @GetMapping("/pausas/tiempo-total")
    Long getTiempoTotalPausaSegundos(@RequestParam("idViaje") String idViaje);

    /**
     * @param monopatin id
     * @return viajes de un determinado monopatin
     */
    @GetMapping("/byMonopatin/{monopatinId}")
    List<ViajeDTO> getViajesByMonopatinId(@PathVariable("monopatinId") String monopatin);

    /**
     * Consulta a Viaje para obtener el conteo de viajes por monopatín, filtrado por año.
     * GET /viajes/reportes/monopatines-por-viajes?year={year}
     */
    @GetMapping("/reportes/monopatines-por-viajes")
    List<ReporteMonopatinContadorViajes> getMonopatinesPorViajes(@RequestParam("year") int year);


    @GetMapping("/reporte-uso")
    ReporteUsoDTO getReporteUso(
            @RequestParam("userIds") List<String> userIds,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    );


    @GetMapping("/reportes/usuarios-uso")
    List<UsuarioUsoDTO> obtenerUsuariosMasActivos(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam("userIds") List<String> userIds
    );

    @GetMapping("/reportes/usuarios-uso")
    List<UsuarioUsoDTO> obtenerUsuariosMasActivos(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam("userIds") String userIdsCsv
    );
}