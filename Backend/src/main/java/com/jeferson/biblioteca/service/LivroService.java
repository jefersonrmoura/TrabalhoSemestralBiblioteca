package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.LivroRequest;
import com.jeferson.biblioteca.dto.LivroResponse;

import java.util.List;

public interface LivroService {
    LivroResponse criar(LivroRequest request);
    List<LivroResponse> listar();
    LivroResponse buscarPorId(String id);
    LivroResponse atualizar(String id, LivroRequest request);
    void deletar(String id);
}
