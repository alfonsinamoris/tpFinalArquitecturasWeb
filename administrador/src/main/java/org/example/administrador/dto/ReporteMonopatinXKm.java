package org.example.administrador.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteMonopatinXKm {
    private String id;
    private Long kmRecorridos;
    private Long tiempoUsoSegundos;
}
