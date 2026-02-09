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

        // --- CAMBIO: No hace falta buscar el artista otra vez, ya lo tenemos en 'usuario'
        Album album = new Album();
        album.setNombre(albumRequest.titulo());
        album.setFoto(albumRequest.imagenUrl());
        album.setPublico(albumRequest.publico()); // Asignamos si es público o no
        album.setFechaLanzamiento(LocalDate.now()); // Ponemos fecha de hoy
        album.setArtista(usuario.getDatosArtista()); // Asignamos el artista dueño

        return convertirADto(repository.save(album));
    }

    public AlbumDto updateAlbum(Long id, AlbumRequest request) {
        Album album = repository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado con ID: " + id));

        // Actualizamos datos básicos
        album.setNombre(request.titulo());
        // Opcional: album.setFechaLanzamiento(LocalDate.now());

        if (request.imagenUrl() != null && !request.imagenUrl().isEmpty()) {
            album.setFoto(request.imagenUrl());
        }
        // Si nos envían el dato de público, lo actualizamos
        if (request.publico() != null) {
            album.setPublico(request.publico());
        }

        Album actualizado = repository.save(album);

        // --- CAMBIO: Construimos el DTO manualmente o reutilizamos métodos para no duplicar lógica
        // Aquí simplificamos la creación del ArtistaBasicDto
        Artista artistaEntity = actualizado.getArtista();
        ArtistaBasicDto artistaBasic = new ArtistaBasicDto(
                String.valueOf(artistaEntity.getId()),
                artistaEntity.getNombre(),
                artistaEntity.getFoto()
        );

        List<CancionBasicDto> cancionesDto = new ArrayList<>();

        if (actualizado.getCanciones() != null) {
            // --- CAMBIO: Usamos el método auxiliar 'convertirCancionADto' para unificar la lógica
            cancionesDto = actualizado.getCanciones().stream()
                    .map(cancion -> convertirCancionADto(cancion, actualizado))
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

        // Vinculamos
        cancion.setAlbum(album);
        cancionRepository.save(cancion);

        // Actualizamos la lista en memoria del álbum para el retorno
        if (!album.getCanciones().contains(cancion)) {
            album.getCanciones().add(cancion);
        }

        // Guardamos el álbum para asegurar consistencia JPA
        repository.save(album);

        // --- CAMBIO: Usamos el método auxiliar para devolver la lista con TODOS los datos (duración, colaboradores, etc)
        return album.getCanciones().stream()
                .map(c -> convertirCancionADto(c, album))
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

        // Desvinculamos
        cancion.setAlbum(null);
        cancionRepository.save(cancion);

        album.getCanciones().remove(cancion);
        repository.save(album);

        // --- CAMBIO: Usamos el método auxiliar
        return album.getCanciones().stream()
                .map(c -> convertirCancionADto(c, album))
                .collect(Collectors.toList());
    }

    public List<AlbumDto> getAlbumsByArtista() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        if (usuario.getDatosArtista() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No eres artista");
        }

        return repository.findAllByArtistaId(usuario.getDatosArtista().getId()).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public List<AlbumDto> getAllAlbums() {
        return repository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public AlbumDto getAlbumById(Long id) {
        return convertirADto(repository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado")));
    }

    // --- MÉTODO PRIVADO: Convierte un Álbum completo a DTO ---
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
            // --- CAMBIO: Usamos el helper para mapear cada canción de la lista
            List<CancionBasicDto> canciones = album.getCanciones().stream()
                    .map(c -> convertirCancionADto(c, album))
                    .collect(Collectors.toList());
            dto.setCanciones(canciones);
        }
        return dto;
    }

    // --- NUEVO MÉTODO AUXILIAR: Mapea una Canción a DTO rellenando los huecos ---
    // Este método centraliza la lógica para que 'duracion', 'genero', y 'colaboradores'
    // se calculen siempre igual, evitando errores en el frontend.
    private CancionBasicDto convertirCancionADto(Cancion cancion, Album album) {

        // 1. Imagen: Si la canción no tiene, usamos la del álbum (Fallback)
        String imagen = (cancion.getFoto() != null && !cancion.getFoto().isEmpty())
                ? cancion.getFoto()
                : album.getFoto();

        // 2. Género: Control de nulos
        String genero = cancion.getGenero() != null ? cancion.getGenero().name() : "POP";

        // 3. Autor: Control de nulos
        String autor = cancion.getAutor() != null ? cancion.getAutor().getNombre() : "Desconocido";

        // 4. Colaboradores: Convertimos la lista de entidades a DTOs básicos
        List<ArtistaBasicDto> colaboradores = new ArrayList<>();
        if (cancion.getColaboraciones() != null) {
            colaboradores = cancion.getColaboraciones().stream()
                    .map(colab -> new ArtistaBasicDto(
                            String.valueOf(colab.getArtistaColaborador().getId()),
                            colab.getArtistaColaborador().getNombre(),
                            colab.getArtistaColaborador().getFoto()
                    ))
                    .collect(Collectors.toList());
        }

        // 5. Retorno: Aquí ponemos '0' en duración porque no existe en la BD.
        // El frontend recibirá 0 y pintará "--:--".
        return new CancionBasicDto(
                String.valueOf(cancion.getId()),
                cancion.getTitulo(),
                imagen,
                genero,
                0, // <--- Duración 'dummy'
                autor,
                colaboradores
        );
    }
}