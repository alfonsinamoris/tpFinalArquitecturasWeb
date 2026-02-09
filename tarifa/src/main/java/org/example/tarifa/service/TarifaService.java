package org.example.tarifa.service;

import org.example.tarifa.entity.Tarifa;
import org.example.tarifa.repository.TarifaRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
@Service
public class TarifaService {

    private TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> getAll(){

        return tarifaRepository.findAll();
    }

    public Tarifa save(Tarifa tarifa){
        Tarifa tarifaNew;
        tarifaNew = tarifaRepository.save(tarifa);
        return tarifaNew;
    }
    public void delete(Tarifa tarifa){
        tarifaRepository.delete(tarifa);
    }

    public Tarifa findById(String id){
        return tarifaRepository.findById(id).orElse(null);
    }

    public Tarifa update(Tarifa tarifa){
        return tarifaRepository.save(tarifa);
    }


    public void updateConFecha(Tarifa tarifa, LocalDate fechaActivacion) {
        Tarifa tarifaActual = tarifaRepository.findById(tarifa.getId()).orElse(tarifa);

        if (tarifaActual == null) {
            throw new RuntimeException("Recurso no encontrado: La tarifa con ID " + tarifa.getId() + " no existe.");
        }

        tarifaActual.setValorComun(tarifa.getValorComun());
        tarifaActual.setValorPremium(tarifa.getValorPremium());
        tarifaActual.setValorExtrapausa(tarifa.getValorExtrapausa());
        tarifaActual.setFechaActivacion(fechaActivacion);

        this.update(tarifaActual);
    }
    public Tarifa registrarNuevaTarifa(Tarifa tarifa, LocalDate fechaActivacion) {
      Tarifa nuevaTarifa = new Tarifa(
                null, // Id debe ser null para que MongoDB genere uno nuevo.
                tarifa.getValorComun(),
                tarifa.getValorPremium(),
                tarifa.getValorExtrapausa(),
                fechaActivacion // Se guarda la fecha de activación futura.
        );
        return tarifaRepository.save(nuevaTarifa);
    }

    public Tarifa findVigenteByDate(LocalDate fechaConsulta) {

        List<Tarifa> tarifasActivas = tarifaRepository.findByFechaActivacionLessThanEqual(fechaConsulta);


        if (tarifasActivas.isEmpty()) {
            return null;
        }

        return tarifasActivas.stream()
                .max(Comparator.comparing(Tarifa::getFechaActivacion))
                .orElse(null);
    }
}

