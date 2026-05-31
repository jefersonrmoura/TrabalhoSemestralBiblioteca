package com.jeferson.biblioteca.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoRequest(
        @NotBlank(message = "livroId é obrigatório")
        String livroId,

        @NotNull(message = "dataDevolucaoPrevista é obrigatória")
        @Future(message = "Data de devolução deve ser futura")
        LocalDate dataDevolucaoPrevista
) {}
