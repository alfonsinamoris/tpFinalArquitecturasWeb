package org.example.viaje.dto;

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

    public UsuarioUsoDTO(String id, Double kmRecorridos) {
        this.id = id;
        this.kmRecorridos = kmRecorridos;
    }
}