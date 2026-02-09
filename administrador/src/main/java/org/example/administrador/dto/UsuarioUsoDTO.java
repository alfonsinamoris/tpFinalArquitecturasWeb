package org.example.administrador.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUsoDTO {
    private String id;
    private String nombre;
    private String rol;
    private Double kmRecorridos;
}