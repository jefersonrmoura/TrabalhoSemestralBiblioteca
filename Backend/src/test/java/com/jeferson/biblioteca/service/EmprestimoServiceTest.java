package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.EmprestimoRequest;
import com.jeferson.biblioteca.dto.EmprestimoResponse;
import com.jeferson.biblioteca.exception.BusinessException;
import com.jeferson.biblioteca.exception.ResourceNotFoundException;
import com.jeferson.biblioteca.model.*;
import com.jeferson.biblioteca.repository.EmprestimoRepository;
import com.jeferson.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private EmprestimoServiceImpl emprestimoService;

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario("João", "joao@email.com", "hash", Role.USUARIO);
        usuario.setId("user1");
        return usuario;
    }

    @Test
    void criarEmprestimo_comLivroDisponivel_retornaEmprestimo() {
        Livro livro = new Livro();
        livro.setId("livro1");
        livro.setQuantidade(2);

        when(livroRepository.findById("livro1")).thenReturn(Optional.of(livro));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Emprestimo empSalvo = new Emprestimo();
        empSalvo.setId("emp1");
        empSalvo.setLivroId("livro1");
        empSalvo.setUsuarioId("user1");
        empSalvo.setNomeUsuario("João");
        empSalvo.setDataEmprestimo(LocalDate.now());
        empSalvo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
        empSalvo.setStatus(StatusEmprestimo.ATIVO);

        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(empSalvo);

        EmprestimoRequest request = new EmprestimoRequest("livro1", LocalDate.now().plusDays(7));
        EmprestimoResponse response = emprestimoService.criar(request, criarUsuario());

        assertEquals("emp1", response.id());
        assertEquals("ATIVO", response.status());
        assertEquals(1, livro.getQuantidade());
    }

    @Test
    void criarEmprestimo_semEstoque_lancaExcecao() {
        Livro livro = new Livro();
        livro.setId("livro1");
        livro.setQuantidade(0);

        when(livroRepository.findById("livro1")).thenReturn(Optional.of(livro));

        EmprestimoRequest request = new EmprestimoRequest("livro1", LocalDate.now().plusDays(7));

        assertThrows(BusinessException.class, () -> emprestimoService.criar(request, criarUsuario()));
    }

    @Test
    void criarEmprestimo_livroInexistente_lancaExcecao() {
        when(livroRepository.findById("xyz")).thenReturn(Optional.empty());

        EmprestimoRequest request = new EmprestimoRequest("xyz", LocalDate.now().plusDays(7));

        assertThrows(ResourceNotFoundException.class, () -> emprestimoService.criar(request, criarUsuario()));
    }
}
