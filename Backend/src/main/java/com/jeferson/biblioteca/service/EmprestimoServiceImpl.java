package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.EmprestimoRequest;
import com.jeferson.biblioteca.dto.EmprestimoResponse;
import com.jeferson.biblioteca.exception.BusinessException;
import com.jeferson.biblioteca.exception.ResourceNotFoundException;
import com.jeferson.biblioteca.model.*;
import com.jeferson.biblioteca.repository.EmprestimoRepository;
import com.jeferson.biblioteca.repository.LivroRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;

    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository,
                                 LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
    }

    @Override
    public EmprestimoResponse criar(EmprestimoRequest request, Usuario usuario) {
        Livro livro = livroRepository.findById(request.livroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        if (livro.getQuantidade() <= 0) {
            throw new BusinessException("Livro não disponível");
        }

        livro.setQuantidade(livro.getQuantidade() - 1);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivroId(request.livroId());
        emprestimo.setUsuarioId(usuario.getId());
        emprestimo.setNomeUsuario(usuario.getNome());
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(request.dataDevolucaoPrevista());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        emprestimo = emprestimoRepository.save(emprestimo);
        return toResponse(emprestimo);
    }

    @Override
    public List<EmprestimoResponse> listar(Usuario usuario) {
        List<Emprestimo> emprestimos;
        if (usuario.getRole() == Role.ADMIN) {
            emprestimos = emprestimoRepository.findAll();
        } else {
            emprestimos = emprestimoRepository.findByUsuarioId(usuario.getId());
        }
        return emprestimos.stream().map(this::toResponse).toList();
    }

    @Override
    public EmprestimoResponse devolver(String id, Usuario usuario) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado"));

        if (!emprestimo.getUsuarioId().equals(usuario.getId()) && usuario.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Acesso negado");
        }

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new BusinessException("Empréstimo já devolvido");
        }

        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimoRepository.save(emprestimo);

        Livro livro = livroRepository.findById(emprestimo.getLivroId()).orElse(null);
        if (livro != null) {
            livro.setQuantidade(livro.getQuantidade() + 1);
            livroRepository.save(livro);
        }

        return toResponse(emprestimo);
    }

    @Override
    public void deletar(String id) {
        if (!emprestimoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empréstimo não encontrado");
        }
        emprestimoRepository.deleteById(id);
    }

    private EmprestimoResponse toResponse(Emprestimo e) {
        return new EmprestimoResponse(
                e.getId(), e.getLivroId(), e.getUsuarioId(), e.getNomeUsuario(),
                e.getDataEmprestimo(), e.getDataDevolucaoPrevista(),
                e.getDataDevolucao(), e.getStatus().name()
        );
    }
}
