package org.example.viaje.entity;


import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection= "pausas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pausa {
    @Id
    private String  id;
    private Date inicio;
    private Date fin;
    private String idViaje; //Referencia al viaje que pertenece

}
