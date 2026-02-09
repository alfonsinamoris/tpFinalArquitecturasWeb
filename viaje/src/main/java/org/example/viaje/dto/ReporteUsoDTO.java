package org.example.viaje.dto;

import lombok.Data;

@Data
public class ReporteUsoDTO {
    private double totalTiempoMinutos;
    private double totalTiempoConPausaMinutos;
    private double totalKilometros;
}
