package org.example.chat.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chat.utils.JwtUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class DatosUsuarioService {

    private final RestClient restClient;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper; // Para leer el JSON

//Esta clase actúa como el puente entre el Chat y tus otros microservicios (Viaje, Monopatin, Tarifa).
    public DatosUsuarioService(RestClient.Builder builder,
                               JwtUtil jwtUtil,
                               ObjectMapper objectMapper,
                               @Value("${api.gateway.url:http://localhost:8080}") String gatewayUrl) {
        this.restClient = builder.baseUrl(gatewayUrl).build();
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }
    @Tool(name = "historialViajes", description = "Obtiene el historial de viajes de un usuario. Requiere el ID del usuario como string.")
    public String obtenerHistorialViajes(String idUsuario) {
        try {
            String token = jwtUtil.createToken(idUsuario, "USER");

            return restClient.get()
                    .uri("/viajes/byUser/" + idUsuario) // Ajusta esta ruta según tu Gateway
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            // ayuda al LLM a explicarle al usuario que hubo un problema
            return "No se pudo obtener el historial. Error del sistema: " + e.getMessage();
        }
    }

    @Tool(name = "infoCuentas", description = "Obtiene saldo y estado de la cuenta. Requiere el ID del usuario.")
    public String obtenerInfoCuentas(String idUsuario) {
        try {
            String token = jwtUtil.createToken(idUsuario, "USER");
            return restClient.get()
                    .uri("/cuentas/by-user/" + idUsuario)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error al consultar la cuenta: " + e.getMessage();
        }
    }

    @Tool(name = "listarMonopatines", description = "Obtiene la lista de todos los monopatines disponibles, su ubicación y estado.")
    public String listarMonopatines() {
        try {
            // Para listar monopatines
            String token = jwtUtil.createToken("admin-system", "ADMIN");

            return restClient.get()
                    .uri("/monopatines")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al listar monopatines: " + e.getMessage();
        }
    }

    /**
     * Verifica si el usuario posee alguna cuenta PREMIUM.
     * Consulta al endpoint: GET /cuenta/by-user/{userId}
     */
    public boolean esUsuarioPremium(String idUsuario) {
        try {
            String token = jwtUtil.createToken(idUsuario, "USER");

            String jsonRespuesta = restClient.get()
                    .uri("/cuenta/by-user/" + idUsuario)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);

            // Imprimir qué devuelve el servicio de cuentas
            System.out.println("Respuesta de Cuentas para usuario " + idUsuario + ": " + jsonRespuesta);

            if (jsonRespuesta == null || jsonRespuesta.isEmpty()) {
                return false;
            }

            List<Map<String, Object>> cuentas = objectMapper.readValue(
                    jsonRespuesta,
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            return cuentas.stream().anyMatch(cuenta -> {
                Object tipoObj = cuenta.get("tipoCuenta"); // Usar Object para evitar NullPointerException al castear
                String tipo = (tipoObj != null) ? tipoObj.toString() : "";
                return "PREMIUM".equalsIgnoreCase(tipo);
            });

        } catch (Exception e) {
            System.err.println("Error verificando premium: " + e.getMessage());
            return false;
        }
    }
}
