package org.example.viaje.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParadaDTO {
    private String id;
    private String direccion;
    private String  nombre;
    private float latitud;
    private float longitud;


}
