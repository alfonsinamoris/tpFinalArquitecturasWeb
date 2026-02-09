package org.example.administrador.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacturasXRangoDTO {
    private String idFactura;
    private double total;
}
