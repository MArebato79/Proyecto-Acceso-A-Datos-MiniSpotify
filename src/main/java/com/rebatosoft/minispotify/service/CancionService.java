package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.ArtistaDto;
import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import com.rebatosoft.minispotify.dto.requests.CancionRequest;
import com.rebatosoft.minispotify.entities.componentes.*;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.ColaboracionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.EntradaPlaylistRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.HistorialRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class CancionService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final ColaboracionRepository colaboracionRepository;
    private final CancionRepository cancionRepository;




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

    // De Request a Entidad (Para crear)
    private Cancion convertirAEntidad(CancionRequest request, Artista autor, Album album) {
        Cancion c = new Cancion();
        c.setTitulo(request.titulo());
        c.setGenero(GENEROS.valueOf(request.genero()));
        c.setAutor(autor);
        c.setAlbum(album);
        return c;
    }
}
