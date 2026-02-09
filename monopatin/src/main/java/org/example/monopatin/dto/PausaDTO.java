package org.example.monopatin.dto;

import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PausaDTO {
    private String  id;
    private Date inicio;
    private Date fin;
    private String idViaje;
}
