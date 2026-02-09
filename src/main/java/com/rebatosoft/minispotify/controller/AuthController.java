package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.requests.LoginRequest;
import com.rebatosoft.minispotify.dto.requests.RegisterRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import com.rebatosoft.minispotify.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- NECESARIO
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
    private final PasswordEncoder passwordEncoder;

    // ===================== LOGIN =====================
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        // 1. Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Buscar usuario en BD
        Usuario usuario = usuarioRepository.findByCorreo(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar Token

        Long artistId = (usuario.getDatosArtista() != null) ? usuario.getDatosArtista().getId() : null;

        String token = jwtService.generateToken(usuario, artistId);

        // 4. Construir respuesta manual (Map)
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", usuario.getId());
        response.put("username", usuario.getRealUsername());
        response.put("email", usuario.getCorreo());
        response.put("foto", usuario.getFotoUrl());
        response.put("imagenUrl", usuario.getFotoUrl()); // Por compatibilidad con frontend
        response.put("artistId", artistId);

        return ResponseEntity.ok(response);
    }

    // ===================== REGISTRO =====================
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        // 1. Comprobar si ya existe (Opcional pero recomendado)
        if (usuarioRepository.findByCorreo(request.email()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 2. Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(request.userName());
        usuario.setCorreo(request.email());
        usuario.setContrasena(passwordEncoder.encode(request.password())); // Encriptar pass

        // Asignar rol si tu sistema lo requiere

        // 3. Guardar en BD
        Usuario guardado = usuarioRepository.save(usuario);

        // 4. Generar Token para que haga login directo
        String token = jwtService.generateToken(guardado, null);

        // 5. Construir respuesta igual que en Login
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", guardado.getId());
        response.put("username", guardado.getUsername());
        response.put("email", guardado.getCorreo());
        response.put("foto", guardado.getFotoUrl());
        response.put("imagenUrl", guardado.getFotoUrl());
        response.put("artistId", null);

        return ResponseEntity.ok(response);
    }
}