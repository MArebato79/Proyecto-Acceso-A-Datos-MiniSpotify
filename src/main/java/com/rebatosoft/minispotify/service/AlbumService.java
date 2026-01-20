package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.AlbumDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import com.rebatosoft.minispotify.dto.requests.AlbumRequest;
import com.rebatosoft.minispotify.entities.componentes.Album;
import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class AlbumService {

    private final AlbumRepository repository;
    private final CancionRepository cancionRepository;

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
        // album.setImagenUrl(request.imagenUrl()); // Si tienes campo imagen
        return album;
    }
}
