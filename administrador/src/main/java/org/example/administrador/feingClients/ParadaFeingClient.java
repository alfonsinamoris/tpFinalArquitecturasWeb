package org.example.administrador.feingClients;

import org.example.administrador.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "microservicio-parada", url = "http://localhost:8083/paradas", configuration = FeignClientConfig.class)
public interface ParadaFeingClient {
}
