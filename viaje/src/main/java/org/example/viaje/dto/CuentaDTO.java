package org.example.viaje.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CuentaDTO {

    public String nroCuenta;
    public String tipoCuenta; //  "BASICA", "PREMIUM")
    public boolean estado;    // True = Activa, False = Anulada


    public boolean getEstado() {
        return this.estado;
    }

    public String getId() {
        return this.nroCuenta;
    }
}
