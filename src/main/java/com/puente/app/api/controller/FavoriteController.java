package com.puente.app.api.controller;

import com.puente.app.api.dto.FavoriteResponse;
import com.puente.app.api.mapper.FavoriteMapper;
import com.puente.app.application.FavoriteService;
import com.puente.app.application.UserService;
import com.puente.app.domain.favorite.Favorite;
import com.puente.app.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Favoritos", description = "Gestión de instrumentos favoritos del usuario")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final FavoriteMapper favoriteMapper;

    public FavoriteController(FavoriteService favoriteService, UserService userService, FavoriteMapper favoriteMapper) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.favoriteMapper = favoriteMapper;
    }

    @GetMapping
    @Operation(
        summary = "Listar favoritos", 
        description = "Retorna la lista de instrumentos marcados como favoritos por el usuario autenticado"
    )
    public ResponseEntity<List<FavoriteResponse>> getFavorites(Authentication authentication) {
        User user = getUserFromAuth(authentication);
        
        List<FavoriteResponse> favorites = favoriteService.getFavorites(user.getId()).stream()
            .map(favoriteMapper::toResponse)
            .toList();
        
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{symbol}")
    @Operation(
        summary = "Agregar favorito", 
        description = "Marca un instrumento como favorito. El símbolo debe ser uno de los 20 instrumentos disponibles (ej: AAPL, GOOGL, etc.)"
    )
    public ResponseEntity<FavoriteResponse> addFavorite(
            @PathVariable String symbol,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        
        Favorite favorite = favoriteService.addFavorite(user.getId(), symbol);
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteMapper.toResponse(favorite));
    }

    @DeleteMapping("/{symbol}")
    @Operation(
        summary = "Eliminar favorito", 
        description = "Remueve un instrumento de la lista de favoritos del usuario"
    )
    public ResponseEntity<Void> removeFavorite(
            @PathVariable String symbol,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        
        favoriteService.removeFavorite(user.getId(), symbol);
        return ResponseEntity.noContent().build();
    }

    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}

