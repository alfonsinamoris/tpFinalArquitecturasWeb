package org.example.viaje.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReporteMonopatinContadorViajes {
    private String idMonopatin;
    private Long cantidadViajes;
}
