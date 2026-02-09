package org.example.chat.controller;

import org.example.chat.service.DatosUsuarioService;
import org.example.chat.service.GroqService;
import org.example.chat.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GroqService groqChat;
    private final JwtUtil jwtUtils;
    private final DatosUsuarioService datosUsuarioService;


    public ChatController(GroqService groqChat, JwtUtil jwtUtils, DatosUsuarioService datosUsuarioService) {
        this.groqChat = groqChat;
        this.jwtUtils = jwtUtils;
        this.datosUsuarioService = datosUsuarioService;
    }
    //Es la puerta de entrada. Expone el endpoint REST que recibe los mensajes del usuario.

    @PostMapping("/consultar")
    public ResponseEntity<String> preguntar(@RequestBody String pregunta,
                                            @RequestHeader("Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o ausente");
        }

        try {
            String jwt = token.substring(7);
            String idUsuario = jwtUtils.extractUsername(jwt);

            // la llamada real seria:
            // boolean esPremium = datosUsuarioService.esUsuarioPremium(idUsuario);

            // Forza a que SIEMPRE sea premium:
            boolean esPremium = true;

            if (!esPremium) {
                return ResponseEntity.status(403)
                        .body("ACCESO DENEGADO: El servicio de chat es exclusivo para usuarios con cuentas PREMIUM.");
            }

            String respuesta = groqChat.getGroqResponse(pregunta, idUsuario);
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(403).body("Error de autorización: " + e.getMessage());
        }
    }
}