package org.example.cuenta.Repository;

import org.example.cuenta.entity.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends MongoRepository<Cuenta,String>{

    List<Cuenta> findByUsuariosContaining(String userId);
}
