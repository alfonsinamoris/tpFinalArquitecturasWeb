package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ParadaApplication {

    public static void main(String[] args) {

        SpringApplication.run(ParadaApplication.class, args);
    }

}
