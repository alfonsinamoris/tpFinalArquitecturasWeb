package org.example.administrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CuentaDTO {
    public Long id;
    public String tipoCuenta; //  "BASICA", "PREMIUM")
    public boolean estado;    // True = Activa, False = Anulada
}
