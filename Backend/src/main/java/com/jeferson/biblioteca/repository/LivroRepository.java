package com.jeferson.biblioteca.repository;

import com.jeferson.biblioteca.model.Livro;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LivroRepository extends MongoRepository<Livro, String> {
}
