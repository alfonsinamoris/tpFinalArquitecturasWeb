package org.example.cuenta.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;
@Document(collection = "cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
    @Id
    private String nroCuenta;
    private double monto;
    private boolean estado;
    private Date fechaAlta;
    private String tipoCuenta;
    private List<String> usuarios ;




}
