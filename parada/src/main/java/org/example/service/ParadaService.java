package org.example.service;

import org.example.dto.MonopatinDTO;
import org.example.entity.Parada;
import org.example.feignClient.MonopatinFeignClient;
import org.example.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParadaService {

   ParadaRepository paradaRepository;

   MonopatinFeignClient monopatinFeignClient;

    public ParadaService(MonopatinFeignClient monopatinFeignClient, ParadaRepository paradaRepository) {
        this.monopatinFeignClient = monopatinFeignClient;
        this.paradaRepository = paradaRepository;
    }

    public List<Parada> getAll(){
        return paradaRepository.findAll();
    }

    public Parada save(Parada parada){
        return paradaRepository.save(parada);
    }
    public boolean deleteById(String id){
        if(paradaRepository.existsById(id)){
            paradaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Parada findById(String id){
        return paradaRepository.findById(id).orElse(null);
    }

    public Parada update(Parada parada){
        return paradaRepository.save(parada);
    }

    /**
     * Lógica de negocio para ubicar un Monopatín en una Parada específica.
     * @param idParada ID de la parada.
     * @param idMonopatin ID del monopatín.
     * @return El DTO del Monopatín actualizado.
     */
    public MonopatinDTO ubicarMonopatin(String idParada, String idMonopatin) {

        // Validar que la parada exista
        Parada parada = paradaRepository.findById(idParada).orElse(null);
        if (parada == null) {
            throw new RuntimeException("Parada no encontrada con ID: " + idParada);
        }

        MonopatinDTO updateDTO = new MonopatinDTO();
        updateDTO.setId(idMonopatin);
        updateDTO.setIdParadaUbicacion(idParada);
        updateDTO.setEstado("disponible");
        updateDTO.setLatitud(parada.getLatitud());
        updateDTO.setLongitud(parada.getLongitud());

        try {
            return monopatinFeignClient.updateMonopatin(idMonopatin,updateDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al ubicar el Monopatín en el MS Monopatín: " + e.getMessage());
        }
    }


}
