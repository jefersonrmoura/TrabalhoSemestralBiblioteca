package com.jeferson.biblioteca.controller;

import com.jeferson.biblioteca.dto.EmprestimoRequest;
import com.jeferson.biblioteca.dto.EmprestimoResponse;
import com.jeferson.biblioteca.model.Usuario;
import com.jeferson.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@Tag(name = "Empréstimos", description = "Gerenciamento de empréstimos e devoluções")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @Operation(summary = "Registrar empréstimo")
    public ResponseEntity<EmprestimoResponse> criar(@Valid @RequestBody EmprestimoRequest request,
                                                    @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criar(request, usuario));
    }

    @GetMapping
    @Operation(summary = "Listar empréstimos (admin: todos, usuario: próprios)")
    public ResponseEntity<List<EmprestimoResponse>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(emprestimoService.listar(usuario));
    }

    @PatchMapping("/{id}/devolver")
    @Operation(summary = "Devolver livro")
    public ResponseEntity<EmprestimoResponse> devolver(@PathVariable String id,
                                                       @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(emprestimoService.devolver(id, usuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar empréstimo (admin)")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        emprestimoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
