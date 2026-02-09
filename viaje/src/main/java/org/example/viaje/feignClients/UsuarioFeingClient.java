package org.example.viaje.feignClients;

import org.example.viaje.dto.UsuarioUsoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



public interface UsuarioFeingClient {

    @GetMapping("/usuarios/{id}")
    UsuarioUsoDTO getUsuarioById(@PathVariable("id") String id);

}
