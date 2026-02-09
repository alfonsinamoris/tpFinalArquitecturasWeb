package org.example.repository;


import org.example.entity.Facturacion;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FacturacionRepository extends MongoRepository<Facturacion, String> {
    List<Facturacion> findByUsuarioId(String usuarioId);

    List<Facturacion> findByFechaBetween(Date inicio, Date fin);
}
