package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.requests.LoginRequest;
import com.rebatosoft.minispotify.dto.requests.RegisterRequest; // Si quieres mover el registro aquí
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import com.rebatosoft.minispotify.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.email()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        Long artistId = (usuario.getDatosArtista() != null) ? usuario.getDatosArtista().getId() : null;
        String token = jwtService.generateToken(userDetails, artistId);

        // CREAMOS UNA RESPUESTA COMPLETA
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", usuario.getId());
        userData.put("username", usuario.getUsername());
        userData.put("email", usuario.getCorreo());
        userData.put("artistId", artistId);

        response.put("user", userData);

        return ResponseEntity.ok(response);
    }
}