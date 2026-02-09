package org.example.usuario.dto;

import lombok.Data;

@Data
public class monopatinDto {
    private String id;
    private String estado;
    private float latitud;
    private float longitud;
    private float kmRecorridos;
    private long tiempoUso;
    private String idParadaUbicacion;
}
