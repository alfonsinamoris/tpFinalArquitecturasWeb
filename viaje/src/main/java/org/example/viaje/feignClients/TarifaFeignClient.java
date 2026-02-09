package org.example.viaje.feignClients;

import org.example.viaje.dto.TarifaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "microservicio-tarifa", url = "http://localhost:8087/tarifas")
public interface TarifaFeignClient {




    @GetMapping("/{id}")
    TarifaDTO getTarifaById(@PathVariable("id") String id);

    @GetMapping("/vigente")
    TarifaDTO getTarifaVigente(@RequestParam("fecha")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaConsulta);
}
