package org.example.administrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteMonopatinContadorViajes {
    private String idMonopatin;
    private Long cantidadViajes;
}
