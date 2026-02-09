package org.example.viaje.feignClients;

import org.example.viaje.dto.UsuarioUsoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "microservicio-usuario", url = "http://localhost:8081/usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/{id}")
    UsuarioUsoDTO getUsuarioById(@PathVariable("id") String id);

}
