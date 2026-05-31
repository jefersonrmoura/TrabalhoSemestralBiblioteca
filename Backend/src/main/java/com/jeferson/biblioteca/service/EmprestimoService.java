package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.EmprestimoRequest;
import com.jeferson.biblioteca.dto.EmprestimoResponse;
import com.jeferson.biblioteca.model.Usuario;

import java.util.List;

public interface EmprestimoService {
    EmprestimoResponse criar(EmprestimoRequest request, Usuario usuario);
    List<EmprestimoResponse> listar(Usuario usuario);
    EmprestimoResponse devolver(String id, Usuario usuario);
    void deletar(String id);
}
