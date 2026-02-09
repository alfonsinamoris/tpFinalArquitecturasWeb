package org.example.cuenta.feignClients;
import org.example.cuenta.Dto.usuarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-usuario", url = "http://localhost:8081/cuenta")
public interface usuarioFeignClient {

    /**
     * Obtiene los detalles de la cuenta por su ID
     * Corresponde al GET /cuenta/{id} en  Cuenta.
     * @param id El ID de la cuenta (Long).
     * @return El DTO de la Cuenta.
     */
    @GetMapping("/{id}")
    public usuarioDto getCuenta(@PathVariable("id") Long id);


}
