package com.jeferson.biblioteca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LivroRequest(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Autor é obrigatório")
        String autor,

        @NotBlank(message = "ISBN é obrigatório")
        String isbn,

        String editora,
        int ano,
        String genero,

        @Min(value = 0, message = "Quantidade não pode ser negativa")
        int quantidade
) {}
