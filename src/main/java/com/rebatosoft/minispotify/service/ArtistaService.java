package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.ArtistaDto;
import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import com.rebatosoft.minispotify.dto.requests.ArtistaRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final UsuarioRepository usuarioRepository;

    public ArtistaDto createPerfilArtista(ArtistaRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getDatosArtista() != null) {
            throw new RuntimeException("¡Ya tienes un perfil de artista creado!");
        }

        Artista artista = convertirAEntidad(request, usuario);

        artista = artistaRepository.save(artista);

        return convertirADto(artista);
    }

    public ArtistaDto getArtistaById(Integer id) {
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado con ID: " + id));
        return convertirADto(artista);
    }

    public ArtistaDto updateArtista(ArtistaRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Artista artista = usuario.getDatosArtista();
        if (artista == null) {
            throw new RuntimeException("No eres artista, no puedes editar nada.");
        }

        artista.setNombre(request.nombre());
        artista.setBiografia(request.biografia());
        artista.setFoto(request.imagenUrl());

        artista = artistaRepository.save(artista);
        return convertirADto(artista);
    }


    public List<ArtistaDto> getAllArtistas() {
        return artistaRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ArtistaDto convertirADto(Artista artista) {
        ArtistaDto dto = new ArtistaDto();
        dto.setId(artista.getId().toString());
        dto.setNombre(artista.getNombre());
        dto.setImagenUrl(artista.getFoto());
        dto.setBiografia(artista.getBiografia());

        if (artista.getAlbums() != null) {
            List<AlbumBasicDto> albumesBasic = artista.getAlbums().stream()
                    .map(album -> {
                        AlbumBasicDto basic = new AlbumBasicDto();
                        basic.setId(album.getId().toString());
                        basic.setNombre(album.getNombre());
                        basic.setImagenUrl(album.getFoto());
                        return basic;
                    })
                    .collect(Collectors.toList());
            dto.setAlbums(albumesBasic);
        }

        return dto;
    }

    private Artista convertirAEntidad(ArtistaRequest request, Usuario usuarioOwner) {
        Artista a = new Artista();
        a.setNombre(request.nombre());
        a.setBiografia(request.biografia());
        a.setFoto(request.imagenUrl());

        a.setUsuarioPropietario(usuarioOwner);

        return a;
    }
}
