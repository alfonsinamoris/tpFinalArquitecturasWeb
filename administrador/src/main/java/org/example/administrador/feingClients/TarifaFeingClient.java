package org.example.administrador.feingClients;

import org.example.administrador.config.FeignClientConfig;
import org.example.administrador.dto.TarifaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "microservicio-tarifa", url = "http://localhost:8087/tarifas", configuration = FeignClientConfig.class)
public interface TarifaFeingClient {

    @PostMapping("/ajuste")
    void actualizarTarifas(@RequestBody TarifaDTO tarifaDTO,
                           @RequestParam("fechaActivacion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaActivacion);


}