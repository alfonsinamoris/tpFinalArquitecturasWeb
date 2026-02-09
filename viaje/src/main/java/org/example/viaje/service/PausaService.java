package org.example.viaje.service;

import org.example.viaje.entity.Pausa;
import org.example.viaje.feignClients.TarifaFeignClient;
import org.example.viaje.repository.PausaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PausaService {
    PausaRepository pausaRepository;

    TarifaFeignClient tarifaFeignClient;

    public PausaService(PausaRepository pausaRepository, TarifaFeignClient tarifaFeignClient) {
        this.pausaRepository = pausaRepository;
        this.tarifaFeignClient = tarifaFeignClient;
    }

    public Pausa save(Pausa pausa){
        return pausaRepository.save(pausa);
    }
    public Pausa findById(String id) {
        return pausaRepository.findById(id).orElse(null);
    }
    public List<Pausa> findByViajeId(String idViaje) {
        return pausaRepository.findByIdViaje(idViaje);
    }

    /**
     * Inicia una nueva pausa asociada a un viaje.
     * @param idViaje ID del viaje.
     * @return La Pausa registrada.
     */
    public Pausa iniciarPausa(String idViaje) {
        Pausa pausa = new Pausa();
        pausa.setIdViaje(idViaje);
        pausa.setInicio(new Date());
        return pausaRepository.save(pausa);
    }

    /**
     * Finaliza una pausa y registra el tiempo total.
     * @param idPausa ID de la pausa a finalizar.
     * @return La Pausa actualizada.
     */
    public Pausa finalizarPausa(String idPausa) {
        Pausa pausa = pausaRepository.findById(idPausa).orElseThrow(() -> new RuntimeException("Pausa no encontrada."));

        //Registrar fin de pausa
        pausa.setFin(new Date());

        // Calcular la duración en minutos
        long duracionEnSeg = pausa.getFin().getTime() - pausa.getInicio().getTime();
        long duracionEnminutos = TimeUnit.MILLISECONDS.toMinutes(duracionEnSeg);

        // Aplicar lógica de multa si excede los 15 minutos
        if (duracionEnminutos > 15) {
            System.out.println("ADVERTENCIA: Pausa del viaje " + pausa.getIdViaje() + " ha excedido los 15 minutos (" + duracionEnminutos + " min).");

        }

        return pausaRepository.save(pausa);
    }

    /**
     * Calcula el tiempo total acumulado de pausas para un viaje específico.
     * Solo suma las pausas que tienen fecha de finalización.
     * @param idViaje ID del viaje.
     * @return Tiempo total de pausa en segundos (Long).
     */
    public Long calcularTiempoTotalPausaSegundos(String idViaje) {
        // Obtener todas las pausas asociadas a ese viaje
        List<Pausa> pausas = pausaRepository.findByIdViaje(idViaje);
        if (pausas.isEmpty()) {
            return 0L;
        }

        // Sumar la duración de todas las pausas finalizadas
        long tiempoTotalSegundos = pausas.stream()
                .filter(pausa -> pausa.getFin() != null)
                .mapToLong(pausa -> {
                    long diffInMillis = pausa.getFin().getTime() - pausa.getInicio().getTime();
                    return TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
                })
                .sum();

        return tiempoTotalSegundos;
    }
}
