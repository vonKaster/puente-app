package com.puente.app.api.controller;

import com.puente.app.api.dto.AuthResponse;
import com.puente.app.api.dto.LoginRequest;
import com.puente.app.api.dto.RegisterRequest;
import com.puente.app.application.AuthService;
import com.puente.app.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints de autenticación para registro e inicio de sesión")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario", 
        description = "Crea una nueva cuenta de usuario con rol USER y retorna el token JWT"
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.getName(), request.getEmail(), request.getPassword());
        String token = authService.login(request.getEmail(), request.getPassword());
        
        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión", 
        description = "Autentica al usuario y retorna un token JWT válido por 24 horas"
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        
        User user = authService.getUserByEmail(request.getEmail());
        
        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getName());
        return ResponseEntity.ok(response);
    }
}

