package org.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Document(collection = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facturacion {
    @Id
    private String idFactura;
    private Date fecha;
    private String viajeId;
    private String usuarioId;
    private double total;

}
