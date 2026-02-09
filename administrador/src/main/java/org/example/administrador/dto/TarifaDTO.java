package org.example.administrador.dto;

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
    private int valorComun;
    private int valorPremium;
    private int valorExtraPausa;
    private LocalDate fechaActivacion;
}
