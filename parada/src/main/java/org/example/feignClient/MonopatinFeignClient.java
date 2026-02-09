package org.example.feignClient;

import org.example.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-monopatin", url = "http://localhost:8082/monopatines")
public interface MonopatinFeignClient {

    /**
     * Actualizacion de la parada donde se encuentra un monopatin
     * PUT /monopatines en el microservicio monopatin
     * @param MonopatinDTO con id y la nueva ubicacon y estado
     * @return MonopatinDTO actualizado
     */
    @PutMapping("/{id}")
    MonopatinDTO updateMonopatin (@PathVariable("id") String id, @RequestBody MonopatinDTO monopatinDTO);
}
