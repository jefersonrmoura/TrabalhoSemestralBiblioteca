package com.jeferson.biblioteca.dto;

public record LivroResponse(
        String id,
        String titulo,
        String autor,
        String isbn,
        String editora,
        int ano,
        String genero,
        int quantidade
) {}
