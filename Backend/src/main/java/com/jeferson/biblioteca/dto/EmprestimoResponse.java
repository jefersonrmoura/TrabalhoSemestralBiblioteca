package com.jeferson.biblioteca.dto;

import java.time.LocalDate;

public record EmprestimoResponse(
        String id,
        String livroId,
        String usuarioId,
        String nomeUsuario,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucaoPrevista,
        LocalDate dataDevolucao,
        String status
) {}
