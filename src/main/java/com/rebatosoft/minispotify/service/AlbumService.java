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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

        Optional<Artista> artista = artistaRepository.findById(usuario.getDatosArtista().getId());

        Album album = new Album();
        album.setNombre(albumRequest.titulo());
        album.setArtista(artista.get());
        album.setFoto(albumRequest.imagenUrl());

        return convertirADto(repository.save(album));
    }

    public AlbumDto updateAlbum(Long id, AlbumRequest request) {
        Album album = repository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado con ID: " + id));

        album.setNombre(request.titulo());
        album.setFechaLanzamiento(LocalDate.now());

        if (request.imagenUrl() != null && !request.imagenUrl().isEmpty()) {
            album.setFoto(request.imagenUrl());
        }
        album.setPublico(request.publico() != null ? request.publico() : album.getPublico());

        Album actualizado = repository.save(album);

        Artista artistaEntity = actualizado.getArtista();
        ArtistaBasicDto artistaBasic = new ArtistaBasicDto(
                String.valueOf(artistaEntity.getId()),
                artistaEntity.getNombre(),
                artistaEntity.getFoto()
        );
        List<CancionBasicDto> cancionesDto = new ArrayList<>();

        if (actualizado.getCanciones() != null) {
            cancionesDto = actualizado.getCanciones().stream()
                    .map(cancion -> new CancionBasicDto(
                            String.valueOf(cancion.getId()),
                            cancion.getTitulo(),
                            (cancion.getFoto() != null && !cancion.getFoto().isEmpty())
                                    ? cancion.getFoto()
                                    : actualizado.getFoto()
                    ))
                    .collect(Collectors.toList());
        }

        return new AlbumDto(
                String.valueOf(actualizado.getId()),
                actualizado.getNombre(),
                actualizado.getFoto(),
                artistaBasic,
                cancionesDto
        );

    }

    @Transactional
    public List<CancionBasicDto> añadirCancionAlbum(Long idCancion, Long idAlbum) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
        }

        Album album = repository.findById(idAlbum.intValue())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado"));

        if (!album.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para editar este Álbum");
        }

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        cancion.setAlbum(album);
        cancionRepository.save(cancion);

        if (!album.getCanciones().contains(cancion)) {
            album.getCanciones().add(cancion);
        }

        return album.getCanciones().stream()
                .map(c -> new CancionBasicDto(
                        String.valueOf(c.getId()),
                        c.getTitulo(),
                        c.getFoto()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CancionBasicDto> eliminarCancionAlbum(Long idCancion, Long idAlbum) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
        }

        Album album = repository.findById(idAlbum.intValue())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado"));

        if (!album.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para editar este Álbum");
        }

        Cancion cancion = cancionRepository.findById(idCancion)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        if (cancion.getAlbum() == null || !cancion.getAlbum().getId().equals(album.getId())) {
            throw new RuntimeException("Esta canción no pertenece al álbum indicado");
        }

        cancion.setAlbum(null);
        cancionRepository.save(cancion);

        album.getCanciones().remove(cancion);

        return album.getCanciones().stream()
                .map(c -> new CancionBasicDto(
                        String.valueOf(c.getId()),
                        c.getTitulo(),
                        c.getFoto()
                ))
                .collect(Collectors.toList());
    }

  public List<AlbumDto> getAlbumsByArtista() {
      String email = SecurityContextHolder.getContext().getAuthentication().getName();
      Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

      if (usuario.getDatosArtista() == null) {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
      }

      List<AlbumDto> albums = repository.findAllByArtistaId(usuario.getDatosArtista().getId()).stream()
              .map(this::convertirADto)
              .collect(Collectors.toList());

      return albums;
   }

    public List<AlbumDto> getAllAlbums() {
        return repository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public AlbumDto getAlbumById(Long id) {
        AlbumDto albumDto = convertirADto(repository.findById(id.intValue()).orElseThrow(null));
        return albumDto;
    }

    private AlbumDto convertirADto(Album album) {
        AlbumDto dto = new AlbumDto();
        dto.setId(album.getId().toString());
        dto.setNombre(album.getNombre());
        dto.setImagenUrl(album.getFoto());

        if (album.getArtista() != null) {
            ArtistaBasicDto artDto = new ArtistaBasicDto();
            artDto.setId(album.getArtista().getId().toString());
            artDto.setNombre(album.getArtista().getNombre());
            artDto.setImagenUrl(album.getArtista().getFoto());
            dto.setArtista(artDto);
        }

        if (album.getCanciones() != null) {
            List<CancionBasicDto> canciones = album.getCanciones().stream()
                    .map(c -> new CancionBasicDto(
                            String.valueOf(c.getId()),
                            c.getTitulo(),
                            (c.getFoto() != null && !c.getFoto().isEmpty()) ? c.getFoto() : album.getFoto()
                    ))
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
