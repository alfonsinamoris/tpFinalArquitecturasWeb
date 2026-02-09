package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonopatinDTO {
    public String id;
    public String estado;
    public float latitud;
    public float longitud;
    public String idParadaUbicacion; //Id de la parada donde se estaciono
}
