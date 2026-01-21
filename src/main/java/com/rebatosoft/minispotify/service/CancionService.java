package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.ArtistaDto;
import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import com.rebatosoft.minispotify.dto.requests.CancionRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.*;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.ColaboracionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.EntradaPlaylistRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.HistorialRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class CancionService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final ColaboracionRepository colaboracionRepository;
    private final CancionRepository cancionRepository;
    private final UsuarioRepository usuarioRepository;


    private CancionDto cambiarEstado(boolean estado, Cancion cancion) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        if(usuario.getDatosArtista()==null) {
            throw new RuntimeException("usuario no permitido");
        }

        if (!cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta canción");
        }

        cancion.setPublica(estado);

        return convertirADto(cancionRepository.save(cancion));

    }

    private CancionDto añadirColaboradores(List<Integer> colaboradoresIds,Integer cancionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        if (usuario.getDatosArtista() == null) {
            throw new RuntimeException("No eres artista");
        }

        Cancion cancion = cancionRepository.findById(Long.valueOf(cancionId))
                .orElseThrow(() -> new RuntimeException("cancion no encontrada"));

        if (!cancion.getAutor().getId().equals(usuario.getDatosArtista().getId())) {
            throw new RuntimeException("No puedes añadir colaboradores a una canción que no es tuya");
        }

        List<Artista> artistasInvitados = artistaRepository.findAllById(colaboradoresIds);

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

        return convertirADto(cancion);
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

    private Cancion convertirAEntidad(CancionRequest request, Artista autor, Album album) {
        Cancion c = new Cancion();
        c.setTitulo(request.titulo());
        c.setGenero(GENEROS.valueOf(request.genero()));
        c.setAutor(autor);
        c.setAlbum(album);
        return c;
    }
}
