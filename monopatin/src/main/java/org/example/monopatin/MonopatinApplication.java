package org.example.monopatin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MonopatinApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonopatinApplication.class, args);
    }

}
