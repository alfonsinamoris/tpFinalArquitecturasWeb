package org.example.monopatin.dto;

import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViajeDTO {
    private String id;
    private Date inicio;
    private Date fin;
    private float kmRecorridos;
    private String idParadaInicio;
    private String idParadaFin;
    private Long idTarifa;
    private String idMonopatin;
    private Long idUsuario;
    private Long idCuenta;
}
