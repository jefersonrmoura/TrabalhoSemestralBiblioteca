package com.jeferson.biblioteca.dto;

import java.util.List;

public record ErroResponse(
        int status,
        String erro,
        List<String> detalhes
) {
    public ErroResponse(int status, String erro) {
        this(status, erro, null);
    }
}
