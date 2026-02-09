package org.example.viaje.feignClients;

import org.example.viaje.dto.FacturacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-facturacion", url = "http://localhost:8086/facturas")
public interface FacturacionFeignClient {

    /**
     * POST /facturas en Facturación para registrar un cobro.
     */
    @PostMapping("")
    FacturacionDTO registrarCobro(@RequestBody FacturacionDTO facturacion);
}