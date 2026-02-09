package org.example.administrador.feingClients;

import org.example.administrador.config.FeignClientConfig;
import org.example.administrador.dto.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "microservicio-monopatin", url = "http://localhost:8082/monopatines", configuration = FeignClientConfig.class)
public interface MonopatinFeingClient {

    /**
     * GET  Obtiene todos los monopatines
     * @return lista de monopatinDTO
     */
    @GetMapping("/")
    ResponseEntity<List<MonopatinDTO>> getAllMonopatines();

    /**
     * PUT cambia el estado del monopatin
     * @param id
     * @return "en mantenimiemto" o "disponible"
     */
    @PutMapping("/{id}/evaluar-mantenimiento")
    ResponseEntity<String> evaluarMantenimiento(@PathVariable("id") String id);



}
