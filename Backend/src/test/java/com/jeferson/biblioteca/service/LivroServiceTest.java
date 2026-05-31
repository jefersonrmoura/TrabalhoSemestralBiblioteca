package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.LivroRequest;
import com.jeferson.biblioteca.dto.LivroResponse;
import com.jeferson.biblioteca.exception.ResourceNotFoundException;
import com.jeferson.biblioteca.model.Livro;
import com.jeferson.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroServiceImpl livroService;

    @Test
    void criarLivro_comDadosValidos_retornaLivroCriado() {
        LivroRequest request = new LivroRequest("Clean Code", "Robert Martin", "978-0132350884", "Prentice Hall", 2008, "Tecnologia", 3);

        Livro livroSalvo = new Livro();
        livroSalvo.setId("abc123");
        livroSalvo.setTitulo("Clean Code");
        livroSalvo.setAutor("Robert Martin");
        livroSalvo.setIsbn("978-0132350884");
        livroSalvo.setEditora("Prentice Hall");
        livroSalvo.setAno(2008);
        livroSalvo.setGenero("Tecnologia");
        livroSalvo.setQuantidade(3);

        when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        LivroResponse response = livroService.criar(request);

        assertNotNull(response.id());
        assertEquals("Clean Code", response.titulo());
        assertEquals(3, response.quantidade());
    }

    @Test
    void buscarPorId_comIdExistente_retornaLivro() {
        Livro livro = new Livro();
        livro.setId("abc123");
        livro.setTitulo("Clean Code");

        when(livroRepository.findById("abc123")).thenReturn(Optional.of(livro));

        LivroResponse response = livroService.buscarPorId("abc123");

        assertEquals("abc123", response.id());
        assertEquals("Clean Code", response.titulo());
    }

    @Test
    void buscarPorId_comIdInexistente_lancaExcecao() {
        when(livroRepository.findById("xyz")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> livroService.buscarPorId("xyz"));
    }
}
