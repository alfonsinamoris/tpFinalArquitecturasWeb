package org.example.administrador.feingClients;


import org.example.administrador.config.FeignClientConfig;
import org.example.administrador.dto.UsuarioDTO;
import org.example.administrador.dto.UsuarioUsoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "microservicio-usuario", url = "http://localhost:8081/usuarios", configuration = FeignClientConfig.class)
public interface UsuarioFeingClient {

    /**
     * GET obtiene la lista de todos los usuarios
     * @return lista de usuarios
     */
    @GetMapping("")
    List<UsuarioDTO> getUsuarios();

    /**
     * GET Obtiene un usuario por id
     * @param id
     * @return usuario por id
     */
    @GetMapping("/{id}")
    UsuarioDTO getUsuarioById(@PathVariable("id") String id);


}
