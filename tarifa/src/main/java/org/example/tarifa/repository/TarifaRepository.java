package org.example.tarifa.repository;

import org.example.tarifa.entity.Tarifa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarifaRepository extends MongoRepository<Tarifa, String> {
    List<Tarifa> findByFechaActivacionLessThanEqual(LocalDate fechaConsulta);
}
