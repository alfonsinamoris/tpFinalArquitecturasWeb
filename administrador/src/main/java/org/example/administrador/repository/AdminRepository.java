package org.example.administrador.repository;

import org.example.administrador.entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin,String> {

}
