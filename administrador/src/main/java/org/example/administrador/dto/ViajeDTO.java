package org.example.administrador.dto;

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
        private String idTarifa;
        private String idMonopatin;
        private String idUsuario;
        private String idCuenta;
}

