package org.example.tarifa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TarifaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarifaApplication.class, args);
    }

}
