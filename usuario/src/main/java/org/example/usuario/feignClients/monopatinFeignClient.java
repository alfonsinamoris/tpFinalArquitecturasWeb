package org.example.usuario.feignClients;
import org.example.usuario.config.FeignClientConfig;
import org.example.usuario.dto.monopatinDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microservicio-monopatin", url = "http://localhost:8082/monopatines", configuration = FeignClientConfig.class)

public interface monopatinFeignClient {

    @PostMapping("/en-paradas")
    List<monopatinDto> getMonopatinesEnParadas(List<String> idParadas);


}
