package org.example.cuenta.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class usuarioDto {
        @Id
        private String id;
        private String nombre;
        private String  mail;
        private int celular;
        private String rol;
        private String contrasenia;
        private List<String> cuentas; //devuelve una lista con los id de esas cuentas
        private List<String> monopatines;
        private List<String> viajes;

}
