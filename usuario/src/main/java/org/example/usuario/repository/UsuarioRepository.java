package org.example.usuario.repository;

import org.example.usuario.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario,String>{

    Optional<Usuario> findByNombre(String nombre);
}
