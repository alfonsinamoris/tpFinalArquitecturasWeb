package org.example.usuario.feignClients;
import org.example.usuario.config.FeignClientConfig;
import org.example.usuario.dto.reporteUsoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "microservicio-viaje", url = "http://localhost:8084/viajes", configuration = FeignClientConfig.class)
public interface viajeFeignClient {

    @GetMapping("/reporte-uso")
    reporteUsoDto getReporteUso(
            @RequestParam("userIds") List<String> userIds,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    );
}
