package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.ArtistaDto;
import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.ColaboracionDto;
import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import com.rebatosoft.minispotify.dto.requests.CancionRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.*;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.ColaboracionRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CancionService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final ColaboracionRepository colaboracionRepository;
    private final CancionRepository cancionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CancionDto crearCancion(CancionRequest request) { // Hazlo public
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getDatosArtista() == null) {
            throw new RuntimeException("No eres artista para crear canciones");
        }

        Cancion cancion = new Cancion();
        cancion.setTitulo(request.titulo());
        cancion.setPublica(request.publica());

        try {
            cancion.setGenero(GENEROS.valueOf(request.genero().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Género no válido");
        }
        cancion.setAutor(usuario.getDatosArtista());

        if (request.albumId() != null) {
            Album album = albumRepository.findById(Integer.valueOf(request.albumId()))
                    .orElseThrow(() -> new RuntimeException("Álbum no encontrado"));

            if (!album.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
                throw new RuntimeException("No puedes añadir canciones a un álbum que no es tuyo");
            }
            cancion.setAlbum(album);
        }

        cancion = cancionRepository.save(cancion);

        if (request.colaboradores() != null && !request.colaboradores().isEmpty()) {

            List<Colaboracion> listaProcesada = request.colaboradores();

            for (Colaboracion colab : listaProcesada) {
                colab.setCancion(cancion);

                if (colab.getArtistaColaborador() != null) {
                    Long idArtista = colab.getArtistaColaborador().getId();
                    Optional<Artista> artistaReal = artistaRepository.findById(idArtista);
                    colab.setArtistaColaborador(artistaReal.get());
                }
            }

            colaboracionRepository.saveAll(listaProcesada);
            cancion.setColaboraciones(listaProcesada);
        }
        return convertirADto(cancion);
    }

    @Transactional
    public CancionDto cambiarEstado(boolean estado, Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        if(usuario.getDatosArtista()==null) {
            throw new RuntimeException("usuario no permitido");
        }
        Cancion cancion = cancionRepository.findById(id).orElseThrow(() -> new RuntimeException("cancion no encontrada"));

        if (!cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta canción");
        }

        cancion.setPublica(estado);

        return convertirADto(cancionRepository.save(cancion));

    }

    @Transactional
    public CancionDto updateCancion(Long id, CancionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        if (usuario.getDatosArtista() == null ||
                !cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para editar esta canción");
        }

        cancion.setTitulo(request.titulo());
        cancion.setGenero(GENEROS.valueOf(String.valueOf(request)));


        if (request.albumId() != null) {

            Album nuevoAlbum = albumRepository.findById(Integer.valueOf(request.albumId()))
                    .orElseThrow(() -> new RuntimeException("Álbum no encontrado"));

            if (!nuevoAlbum.getArtista().getId().equals(usuario.getDatosArtista().getId())) {
                throw new RuntimeException("No puedes mover la canción a un álbum que no es tuyo");
            }
            cancion.setAlbum(nuevoAlbum);
        } else {
            cancion.setAlbum(null);
        }

        return convertirADto(cancionRepository.save(cancion));
    }




    private CancionDto convertirADto(Cancion cancion) {
        CancionDto dto = new CancionDto();
        dto.setId((long) cancion.getId());
        dto.setTitulo(cancion.getTitulo());
        dto.setGenero(cancion.getGenero() != null ? cancion.getGenero().toString() : null);

        if (cancion.getAutor() != null) {
            ArtistaDto artDto = new ArtistaDto();
            artDto.setId(cancion.getAutor().getId().toString());
            artDto.setNombre(cancion.getAutor().getNombre());
            dto.setArtista(artDto);
        }

        if (cancion.getAlbum() != null) {
            AlbumBasicDto albumBasic = new AlbumBasicDto();
            albumBasic.setId(cancion.getAlbum().getId().toString());
            albumBasic.setNombre(cancion.getAlbum().getNombre());
            dto.setAlbum(albumBasic);
        }
        return dto;
    }

    private ColaboracionDto convertirAColaboracionDto(Colaboracion colab) {

        ColaboracionDto dto = new ColaboracionDto();

        if (colab.getArtistaColaborador() != null) {
            dto.setArtistaId(String.valueOf(colab.getArtistaColaborador().getId()));
            dto.setNombre(colab.getArtistaColaborador().getNombre());
            dto.setImagenUrl(colab.getArtistaColaborador().getFoto());
            dto.setRol(colab.getRol());
        }

        dto.setRol(colab.getRol()); // "Feat", etc.
        return dto;
    }

    private Cancion convertirAEntidad(CancionRequest request, Artista autor, Album album) {
        Cancion c = new Cancion();
        c.setTitulo(request.titulo());
        c.setGenero(GENEROS.valueOf(request.genero()));
        c.setAutor(autor);
        c.setAlbum(album);
        return c;
    }

    @Transactional
    public void deleteCancion(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));


        if (usuario.getDatosArtista() == null ||
                !cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para borrar esta canción");
        }
        cancionRepository.delete(cancion);
    }

    //Gestion de colaboradores

    @Transactional
    public List<ColaboracionDto> agregarColaboradores(List<Integer> colaboradoresIds, Integer cancionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getDatosArtista() == null) {
            throw new RuntimeException("No eres artista");
        }

        Cancion cancion = cancionRepository.findById(Long.valueOf(cancionId))
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        if (!cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No puedes añadir colaboradores a una canción que no es tuya");
        }
        List<Long> idsLong = colaboradoresIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());

        List<Artista> artistasInvitados = artistaRepository.findAllById(idsLong);

        List<Colaboracion> nuevasColaboraciones = artistasInvitados.stream()
                .filter(artista -> !artista.getId().equals(usuario.getDatosArtista().getId()))
                .map(artistaInvitado -> {
                    Colaboracion colab = new Colaboracion();
                    colab.setArtistaColaborador(artistaInvitado);
                    colab.setCancion(cancion);
                    colab.setRol("Feat");
                    return colab;
                })
                .collect(Collectors.toList());

        colaboracionRepository.saveAll(nuevasColaboraciones);

        if (cancion.getColaboraciones() == null) {
            cancion.setColaboraciones(nuevasColaboraciones);
        } else {
            cancion.getColaboraciones().addAll(nuevasColaboraciones);
        }
        return cancion.getColaboraciones().stream()
                .map(this::convertirAColaboracionDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ColaboracionDto> eliminarColaborador(Long cancionId, Long artistaColaboradorId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Cancion cancion = cancionRepository.findById(cancionId).orElseThrow();

        if (!cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No autorizado");
        }

        boolean borrado = cancion.getColaboraciones().removeIf(c ->
                c.getArtistaColaborador().getId().equals(artistaColaboradorId.intValue())
        );

        if (!borrado) {
            throw new RuntimeException("El colaborador no estaba en la canción");
        }

        cancionRepository.save(cancion);

        return cancion.getColaboraciones().stream()
                .map(this::convertirAColaboracionDto)
                .collect(Collectors.toList());
    }



    // metodos de busqueda

    public List<CancionDto> searchCanciones(String termino) {
        return cancionRepository.findByTituloContainingIgnoreCase(termino).stream()
                .filter(Cancion::isPublica)
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public CancionDto getCancionById(Long id) {
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        if (!cancion.isPublica()) {
            // Si es privada, solo el dueño debería poder verla.
            // Aquí podrías meter lógica de seguridad extra.
        }

        return convertirADto(cancion);
    }
}
