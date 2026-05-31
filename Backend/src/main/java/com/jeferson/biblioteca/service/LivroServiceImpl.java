package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.LivroRequest;
import com.jeferson.biblioteca.dto.LivroResponse;
import com.jeferson.biblioteca.exception.ResourceNotFoundException;
import com.jeferson.biblioteca.model.Livro;
import com.jeferson.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public LivroResponse criar(LivroRequest request) {
        Livro livro = new Livro();
        preencherLivro(livro, request);
        livro = livroRepository.save(livro);
        return toResponse(livro);
    }

    @Override
    public List<LivroResponse> listar() {
        return livroRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public LivroResponse buscarPorId(String id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
        return toResponse(livro);
    }

    @Override
    public LivroResponse atualizar(String id, LivroRequest request) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
        preencherLivro(livro, request);
        livro = livroRepository.save(livro);
        return toResponse(livro);
    }

    @Override
    public void deletar(String id) {
        if (!livroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        livroRepository.deleteById(id);
    }

    private void preencherLivro(Livro livro, LivroRequest request) {
        livro.setTitulo(request.titulo());
        livro.setAutor(request.autor());
        livro.setIsbn(request.isbn());
        livro.setEditora(request.editora());
        livro.setAno(request.ano());
        livro.setGenero(request.genero());
        livro.setQuantidade(request.quantidade());
    }

    private LivroResponse toResponse(Livro livro) {
        return new LivroResponse(
                livro.getId(), livro.getTitulo(), livro.getAutor(),
                livro.getIsbn(), livro.getEditora(), livro.getAno(),
                livro.getGenero(), livro.getQuantidade()
        );
    }
}
