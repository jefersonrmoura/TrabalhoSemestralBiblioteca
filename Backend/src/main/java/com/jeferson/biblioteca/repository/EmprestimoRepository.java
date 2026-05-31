package com.jeferson.biblioteca.repository;

import com.jeferson.biblioteca.model.Emprestimo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmprestimoRepository extends MongoRepository<Emprestimo, String> {
    List<Emprestimo> findByUsuarioId(String usuarioId);
}
