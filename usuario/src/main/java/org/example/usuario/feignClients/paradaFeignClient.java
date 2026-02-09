package org.example.usuario.feignClients;

import org.example.usuario.config.FeignClientConfig;
import org.example.usuario.dto.ParadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservicio-parada", url = "http://localhost:8083/paradas", configuration = FeignClientConfig.class)
public interface paradaFeignClient {

    @GetMapping("/")
    List<ParadaDTO> getAllParadas();
}
