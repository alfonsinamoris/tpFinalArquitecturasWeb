package org.example.monopatin.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "monopatines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {
    @Id
    private String id;
    private String estado;
    private float latitud;
    private float longitud;
    private float kmRecorridos;
    private long tiempoUso; //En minutos
    private String idParadaUbicacion; //ID de la Parada si esta estacionado (referencia a microservicio parada)

}
