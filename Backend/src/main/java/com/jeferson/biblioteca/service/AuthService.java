package com.jeferson.biblioteca.service;

import com.jeferson.biblioteca.dto.AuthResponse;
import com.jeferson.biblioteca.dto.LoginRequest;
import com.jeferson.biblioteca.dto.RegistroRequest;

public interface AuthService {
    AuthResponse registrar(RegistroRequest request);
    AuthResponse login(LoginRequest request);
}
