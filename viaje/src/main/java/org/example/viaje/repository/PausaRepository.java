package org.example.viaje.repository;

import org.example.viaje.entity.Pausa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PausaRepository extends MongoRepository<Pausa, String> {
    List<Pausa> findByIdViaje(String idViaje);
}
