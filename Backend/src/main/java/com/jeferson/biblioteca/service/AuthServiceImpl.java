package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.AuthResponse;
import com.jeferson.biblioteca.dto.LoginRequest;
import com.jeferson.biblioteca.dto.RegistroRequest;
import com.jeferson.biblioteca.exception.BusinessException;
import com.jeferson.biblioteca.exception.ConflictException;
import com.jeferson.biblioteca.model.Role;
import com.jeferson.biblioteca.model.Usuario;
import com.jeferson.biblioteca.repository.UsuarioRepository;
import com.jeferson.biblioteca.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email já cadastrado");
        }

        Role role = usuarioRepository.count() == 0 ? Role.ADMIN : Role.USUARIO;

        Usuario usuario = new Usuario(
                request.nome(),
                request.email(),
                passwordEncoder.encode(request.senha()),
                role
        );
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmail(), usuario.getRole().name());
        return new AuthResponse(token, usuario.getNome(), usuario.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmail(), usuario.getRole().name());
        return new AuthResponse(token, usuario.getNome(), usuario.getRole().name());
    }
}
