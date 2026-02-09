package org.example.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "paradas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parada {
    @Id
    private String id;
    private String direccion;
    private String  nombre;
    private float latitud;
    private float longitud;

}
