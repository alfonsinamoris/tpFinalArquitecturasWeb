package org.example.usuario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
    public class Usuario {
        @Id
        private String id;
        private String nombre;
        private String  mail;
        private int celular;
        private String rol;
        private String contrasenia;
        private List<String> cuentas; //me devuelve una lista con los id de esas cuentas
        private List<String> monopatines;
        private List<String> viajes;
    }


