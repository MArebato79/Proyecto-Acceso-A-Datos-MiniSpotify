package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.AlbumDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import com.rebatosoft.minispotify.dto.requests.AlbumRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.*;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class AlbumService {

    private final AlbumRepository repository;
    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AlbumDto crearAlbum(AlbumRequest albumRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no es artista");
        }

        Artista artista = artistaRepository.findById(usuario.getDatosArtista().getId());

        Album album = new Album();
        album.setNombre(albumRequest.titulo());
        album.setArtista(artista);
        album.setFoto(albumRequest.imagenUrl());

        return convertirADto(repository.save(album));
    }

    @Transactional
    public void añadirCancionAlbum(Long idCancion , Long idAlbum){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Album album = repository.findById(Integer.parseInt(String.valueOf(idAlbum)))
                .orElseThrow(() -> new RuntimeException("album no encontrado"));

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
        }

        if (!album.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para editar este Album");
        }

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new RuntimeException("cancion no encontrada"));

       cancion.setAlbum(album);

       cancionRepository.save(cancion);
    }

    @Transactional
    public void eliminarCancionAlbum(Long idCancion , Long idAlbum){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
        }

        Album album = repository.findById(Integer.parseInt(String.valueOf(idAlbum)))
                .orElseThrow(() -> new RuntimeException("album no encontrado"));

        if (!album.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para editar este Album");
        }

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new RuntimeException("cancion no encontrada"));

        if (cancion.getAlbum() == null || !cancion.getAlbum().getId().equals(album.getId())) {
            throw new RuntimeException("Esta canción no pertenece al álbum indicado");
        }

        cancion.setAlbum(null);

        cancionRepository.save(cancion);
    }

    private AlbumDto convertirADto(Album album) {
        AlbumDto dto = new AlbumDto();
        dto.setId(album.getId().toString());
        dto.setNombre(album.getNombre());

        if (album.getArtista() != null) {
            ArtistaBasicDto artDto = new ArtistaBasicDto();
            artDto.setId(album.getArtista().getId().toString());
            artDto.setNombre(album.getArtista().getNombre());
            dto.setArtista(artDto);
        }

        if (album.getCanciones() != null) {
            List<CancionBasicDto> canciones = album.getCanciones().stream()
                    .map(c -> new CancionBasicDto(String.valueOf(c.getId()), c.getTitulo(), null))
                    .collect(Collectors.toList());
            dto.setCanciones(canciones);
        }
        return dto;
    }


    private Album convertirAEntidad(AlbumRequest request, Artista artista) {
        Album album = new Album();
        album.setNombre(request.titulo());
        album.setArtista(artista);
        album.setFoto(request.imagenUrl());
        return album;
    }
}
