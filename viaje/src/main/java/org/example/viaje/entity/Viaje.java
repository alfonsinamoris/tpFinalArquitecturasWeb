package org.example.viaje.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    @Id
    private String id;
    private Date inicio;
    private Date fin;
    private float kmRecorridos;
    private String idParadaInicio;
    private String idParadaFin;
    private String idTarifa;
    private String idMonopatin;
    private String idUsuario;
    private String idCuenta;

}
