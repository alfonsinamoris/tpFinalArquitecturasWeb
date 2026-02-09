package org.example.monopatin.service;

import org.example.monopatin.dto.ViajeDTO;
import org.example.monopatin.feignClient.ViajeFeignClient;
import org.example.monopatin.entity.Monopatin;
import org.example.monopatin.repository.MonopatinRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MonopatinService {

    MonopatinRepository monopatinRepository;
    ViajeFeignClient viajeFeignClient;

    public MonopatinService(MonopatinRepository monopatinRepository, ViajeFeignClient viajeFeignClient) {
        this.monopatinRepository = monopatinRepository;
        this.viajeFeignClient = viajeFeignClient;
    }

    public List<Monopatin> getAll(){
        return monopatinRepository.findAll();
    }
    public Monopatin save(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }
    public void delete(Monopatin monopatin){
        monopatinRepository.delete(monopatin);
    }

    public Monopatin findById(String id){
        return monopatinRepository.findById(id).orElse(null);
    }

    public Monopatin update(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }

    /**
     * Calcula y actualiza el tiempo total de uso y los km totales
     * para verificar si el monopatín requiere de mantenimiento.
     * @param idMonopatin ID del monopatín a evaluar.
     */
    public void evaluarMantenimiento(String idMonopatin) {
        Monopatin monopatin = monopatinRepository.findById(idMonopatin).orElseThrow(() -> new RuntimeException("Monopatín no encontrado."));

        List<ViajeDTO> viajes = viajeFeignClient.getViajesByMonopatinId(idMonopatin);

        float kmTotales = 0;
        long tiempoTotalUsoNetoSegundos = 0;

        for (ViajeDTO viaje : viajes) {
            // Solo considerar viajes finalizados
            if (viaje.getFin() != null) {
                kmTotales += viaje.getKmRecorridos();

                //Calcular duración BRUTA (Inicio a Fin)
                long duracionTotalSegundos = TimeUnit.MILLISECONDS.toSeconds(
                        viaje.getFin().getTime() - viaje.getInicio().getTime()
                );

                //Consultar el tiempo total de pausa desde Viaje
                Long tiempoPausaSegundos = 0L;
                try {
                    tiempoPausaSegundos = viajeFeignClient.getTiempoTotalPausaSegundos(viaje.getId());
                } catch (Exception e) {
                    System.err.println("Advertencia: No se pudo obtener tiempo de pausa para viaje " + viaje.getId());
                }

                //Calcular el tiempo de uso NETO
                long tiempoUsoNeto = duracionTotalSegundos - tiempoPausaSegundos;
                if (tiempoUsoNeto < 0) {
                    tiempoUsoNeto = 0; // Evitar tiempos negativos si hay errores de registro
                }

                tiempoTotalUsoNetoSegundos += tiempoUsoNeto;
            }
        }

        // Actualizar las métricas en la entidad
        monopatin.setKmRecorridos(kmTotales);
        monopatin.setTiempoUso(tiempoTotalUsoNetoSegundos);

        // Mantenimiento si excede X km O Y segundos de uso neto.
        if (monopatin.getKmRecorridos() > 500 || monopatin.getTiempoUso() > 500000) {
            monopatin.setEstado("mantenimiento");
            System.out.println("Monopatín " + idMonopatin + " marcado para mantenimiento.");
        }

        monopatinRepository.save(monopatin);
    }

    /**
     * Busca monopatines disponibles en una lista de IDs de paradas.
     */
    public List<Monopatin> findMonopatinesEnParadas(List<String> idParadas) {
        // Un monopatín solo es 'cercano' si está 'disponible'
        return monopatinRepository.findByEstadoAndIdParadaUbicacionIn("disponible", idParadas);
    }

    public boolean deleteById(String id){
        if (monopatinRepository.existsById(id)) {
            monopatinRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Monopatin updateEstado(String id, String nuevoEstado) {
        Monopatin monopatin = monopatinRepository.findById(id).orElse(null);
        if (monopatin == null) {
            return null;
        }
        monopatin.setEstado(nuevoEstado);
        return monopatinRepository.save(monopatin);
    }

}
