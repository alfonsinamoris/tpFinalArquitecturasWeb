package org.example.apigateway.feignClients;

import org.example.apigateway.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "microservicio-usuario", url = "http://localhost:8081/usuarios")
public interface UsuarioAuthFeignClient {

    /**
     * Consulta el microservicio Usuario para obtener datos de autenticación.
     * GET /usuarios/auth-data?username={username}
     */
    @GetMapping("/auth-data")
    User getUsuarioAuthData(@RequestParam("username") String username);
}
