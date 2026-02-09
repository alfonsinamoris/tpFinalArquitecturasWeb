package org.example.viaje.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TarifaDTO {
    private String id;
    private int valorComun;
    private int valorPremium;
    private int valorExtrapausa;
    private LocalDate fechaActivacion;
}
