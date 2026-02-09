package org.example.viaje.feignClients;

import org.example.viaje.dto.ParadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-parada", url = "http://localhost:8083/paradas")
public interface ParadaFeingClient {
    /**
     * GET /paradas/{id} en el microservicio de paradas
     * Se usua para validar si una para existe y obtener sus coordenadas
     * @param id el ID de la parada
     * @return ParadaDTO
     */
    @GetMapping("/{id}")
    ParadaDTO getParada(@PathVariable("id") String id);
}
