package org.example.viaje.feignClients;

import org.example.viaje.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-monopatin", url = "http://localhost:8082/monopatines")
public interface MonopatinFeignClient {
    /**
     * Obtiene un Monopatin del servicio Monopatin
     * GET /monopatines/{id}
     */
    @GetMapping("{id}")
    MonopatinDTO getMonopatin(@PathVariable("id") String id);

    /**
     * Actualiza el estado de un Monopatin
     * PUT /monopatines/{id} para actualizar la entidad
     */
    @PutMapping("{id}")
    MonopatinDTO updateMonopatin(@PathVariable("id") String id, @RequestBody MonopatinDTO monopatin);

}
