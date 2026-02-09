package org.example.administrador.feingClients;


import org.example.administrador.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "microservicio-facturacion", url = "http://localhost:8086/facturas", configuration = FeignClientConfig.class)
public interface FacturacionFeingClient {

    /**
     * GET Obtiene el total facturado en un rango por parametro
     * @param anio
     * @param mesInicio
     * @param mesFin
     * @return El total facturado en un rango de fecha
     */
    @GetMapping("/total-facturado")
    ResponseEntity<Double> getTotalFacturado(
            @RequestParam("anio") int anio,
            @RequestParam("mesInicio") int mesInicio,
            @RequestParam("mesFin") int mesFin
    );
}
