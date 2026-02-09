package org.example.repository;

import org.example.entity.Parada;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadaRepository extends MongoRepository<Parada,String> {

}
