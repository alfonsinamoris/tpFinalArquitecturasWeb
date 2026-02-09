package org.example.usuario.service;

import feign.FeignException;
import org.example.usuario.dto.ParadaDTO;
import org.example.usuario.dto.cuentaDto;
import org.example.usuario.dto.reporteUsoDto;
import org.example.usuario.entity.Usuario;
import org.example.usuario.dto.monopatinDto;
import org.example.usuario.feignClients.monopatinFeignClient;
import org.example.usuario.feignClients.paradaFeignClient;
import org.example.usuario.repository.UsuarioRepository;
import org.example.usuario.feignClients.cuentaFeignClient;
import org.example.usuario.feignClients.viajeFeignClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;
    cuentaFeignClient cuentaFeignClient;
    monopatinFeignClient monopatinFeignClient;
    viajeFeignClient viajeFeignClient;
    paradaFeignClient paradaFeignClient;
    PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, cuentaFeignClient cuentaFeignClient, monopatinFeignClient monopatinFeignClient, viajeFeignClient viajeFeignClient, paradaFeignClient paradaFeignClient, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaFeignClient = cuentaFeignClient;
        this.monopatinFeignClient = monopatinFeignClient;
        this.viajeFeignClient = viajeFeignClient;
        this.paradaFeignClient = paradaFeignClient;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }


    public Usuario save(Usuario usuario) {
        // Si el usuario tiene una contraseña sin encriptar (raw), se encripta antes de guardar.
        if (usuario.getContrasenia() != null && !usuario.getContrasenia().startsWith("$2a")) { // Verifica si no está encriptada
            usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        }
        return usuarioRepository.save(usuario);
    }
    public void delete(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

    public Usuario findById(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario update(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distancia en km
    }


    public List<monopatinDto> buscarMonopatinesCercanos(double lat, double lng, double radiokm) {

        List<ParadaDTO> todasLasParadas;
        try {
            todasLasParadas = paradaFeignClient.getAllParadas();
        } catch (Exception e) {
            System.err.println("Error al obtener paradas: " + e.getMessage());
            return new ArrayList<>(); // Si falla obtener paradas, devolver lista vacía.
        }

        List<String> paradasCercanasIds = new ArrayList<>();

        for (ParadaDTO parada : todasLasParadas) {
            double distancia = calcularDistanciaHaversine(
                    lat, lng,
                    parada.getLatitud(), parada.getLongitud()
            );

            if (distancia <= radiokm) {
                paradasCercanasIds.add(parada.getId());
            }
        }

        // Si no hay paradas cercanas, devuelve lista vacía
        if (paradasCercanasIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Consultar al microservicio Monopatín por los monopatines en esas paradas
        try {
            return monopatinFeignClient.getMonopatinesEnParadas(paradasCercanasIds);
        } catch (FeignException e) {
            System.err.println("Error de comunicación con MS Monopatín: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error inesperado al buscar monopatines: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public reporteUsoDto getReporteUso(String userId, LocalDate fechaInicio, LocalDate fechaFin, boolean otrosUsuarios) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return null;
        }

        Set<String> usuariosConsultar = new HashSet<>();
        usuariosConsultar.add(userId);

        if (otrosUsuarios){
            for (String nroCuenta: usuario.getCuentas()) {
                List<String> usuariosRelacionados = cuentaFeignClient.getUsuariosAsociados(nroCuenta);
                usuariosConsultar.addAll(usuariosRelacionados);
            }
        }
        return viajeFeignClient.getReporteUso(new ArrayList<>(usuariosConsultar), fechaInicio, fechaFin);
    }


    /**
     * Método SÓLO para el Gateway: Obtiene el usuario incluyendo la contraseña y rol.
     * @param nombreUsername El nombre de usuario.
     * @return Usuario (con contraseña encriptada).
     */
    public Usuario getUsuarioParaAutenticacion(String nombreUsername) {
        return usuarioRepository.findByNombre(nombreUsername).orElse(null);
    }

}
