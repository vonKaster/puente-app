package com.puente.app.api.controller;

import com.puente.app.api.dto.UpdateProfileRequest;
import com.puente.app.api.dto.UserResponse;
import com.puente.app.api.mapper.UserMapper;
import com.puente.app.application.UserService;
import com.puente.app.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Gestión de usuarios y perfiles")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Listar todos los usuarios", 
        description = "Retorna la lista completa de usuarios registrados. Requiere rol ADMIN."
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll().stream()
            .map(userMapper::toResponse)
            .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obtener perfil actual", 
        description = "Retorna la información del perfil del usuario autenticado"
    )
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PutMapping("/me")
    @Operation(
        summary = "Actualizar perfil", 
        description = "Permite al usuario actualizar su nombre y email"
    )
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        User updated = userService.updateProfile(user.getId(), request.getName(), request.getEmail());
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Eliminar usuario", 
        description = "Elimina un usuario por su ID. Requiere rol ADMIN."
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Promover a ADMIN", 
        description = "Otorga rol de administrador a un usuario. Requiere rol ADMIN."
    )
    public ResponseEntity<UserResponse> promoteToAdmin(@PathVariable Long userId) {
        User promoted = userService.promoteToAdmin(userId);
        return ResponseEntity.ok(userMapper.toResponse(promoted));
    }
}

