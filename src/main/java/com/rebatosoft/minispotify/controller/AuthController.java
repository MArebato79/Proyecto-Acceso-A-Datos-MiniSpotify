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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        // 1. Esto autentica contra Spring Security (comprueba usuario y contraseña)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Si pasa la línea anterior, es que las credenciales son buenas. Buscamos al usuario
        Usuario usuario = usuarioRepository.findByCorreo(request.email()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        // 3. Generamos el Token.
        Long artistId = (usuario.getDatosArtista() != null) ? usuario.getDatosArtista().getId() : null;

        String token = jwtService.generateToken(userDetails, artistId);

        // 4. Devolvemos el token en un JSON
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}