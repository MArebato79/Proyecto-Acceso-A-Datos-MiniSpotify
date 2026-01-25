package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.UsuarioDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.requests.RegisterRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.entities.componentes.Follow;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.FollowRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FollowRepository followRepository;
    private final ArtistaRepository artistaRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. REGISTRO DE USUARIO
    public UsuarioDto registerUser(RegisterRequest request) {
        // Validar si el correo ya existe
        if (usuarioRepository.findByCorreo(request.email()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.email());
        usuario.setUsername(request.userName()); // Si tienes este campo
        // Encriptamos la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(request.password()));

        return convertirADto(usuarioRepository.save(usuario));
    }

    // 2. SEGUIR A UN ARTISTA
    public void followArtist(Long artistId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Artista artista = artistaRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));

        // Evitar duplicados (si ya lo sigue)
        if (followRepository.existsByUsuarioSeguidorAndArtistaSeguido(usuario, artista)) {
            throw new RuntimeException("Ya sigues a este artista");
        }

        Follow follow = new Follow();
        follow.setUsuarioSeguidor(usuario);
        follow.setArtistaSeguido(artista);

        followRepository.save(follow);
    }

    // 3. DEJAR DE SEGUIR
    public void unfollowArtist(Long artistId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Artista artista = artistaRepository.findById(artistId)
                .orElseThrow();

        // Buscamos el follow específico
        Follow follow = followRepository.findByUsuarioSeguidorAndArtistaSeguido(usuario, artista)
                .orElseThrow(() -> new RuntimeException("No sigues a este artista"));

        followRepository.delete(follow);
    }

    // 4. VER A QUIÉN SIGO
    public List<ArtistaBasicDto> getFollowedArtists() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        return followRepository.findByUsuarioSeguidor(usuario).stream()
                .map(follow -> {
                    ArtistaBasicDto dto = new ArtistaBasicDto();
                    dto.setId(follow.getArtistaSeguido().getId().toString());
                    dto.setNombre(follow.getArtistaSeguido().getNombre());
                    dto.setImagenUrl(follow.getArtistaSeguido().getFoto());
                    return dto;
                })

                .collect(Collectors.toList());
    }

    private UsuarioDto convertirADto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId().toString());
        dto.setUsername(usuario.getCorreo());
        dto.setEmail(usuario.getCorreo());

        if (usuario.getDatosArtista() != null) {
            ArtistaBasicDto artDto = new ArtistaBasicDto();
            artDto.setId(usuario.getDatosArtista().getId().toString());
            artDto.setNombre(usuario.getDatosArtista().getNombre());
            dto.setPerfilArtista(artDto);
        }
        return dto;
    }

    // Para el registro inicial
    private Usuario convertirAEntidad(RegisterRequest request, String passwordCodificada) {
        Usuario u = new Usuario();
        u.setCorreo(request.email());
        u.setContrasena(passwordCodificada);
        return u;
    }
}
