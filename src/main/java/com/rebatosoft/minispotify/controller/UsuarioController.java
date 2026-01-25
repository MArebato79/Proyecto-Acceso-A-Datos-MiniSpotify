package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.UsuarioDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.requests.RegisterRequest;
import com.rebatosoft.minispotify.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // 1. REGISTRO (Público)
    @PostMapping("/register")
    public ResponseEntity<UsuarioDto> registrarUsuario(@RequestBody @Valid RegisterRequest request) {
        UsuarioDto nuevoUsuario = usuarioService.registerUser(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // 2. SEGUIR ARTISTA
    // URL: POST /usuarios/follow/5
    @PostMapping("/follow/{artistId}")
    public ResponseEntity<Void> followArtist(@PathVariable Long artistId) {
        usuarioService.followArtist(artistId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 3. DEJAR DE SEGUIR ARTISTA
    // URL: DELETE /usuarios/follow/5
    @DeleteMapping("/follow/{artistId}")
    public ResponseEntity<Void> unfollowArtist(@PathVariable Long artistId) {
        usuarioService.unfollowArtist(artistId);
        return ResponseEntity.noContent().build();
    }

    // 4. VER MIS ARTISTAS SEGUIDOS
    @GetMapping("/following")
    public ResponseEntity<List<ArtistaBasicDto>> getFollowedArtists() {
        return ResponseEntity.ok(usuarioService.getFollowedArtists());
    }
}