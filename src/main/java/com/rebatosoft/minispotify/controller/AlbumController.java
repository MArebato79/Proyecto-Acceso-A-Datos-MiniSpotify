package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.AlbumDto;
import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import com.rebatosoft.minispotify.dto.requests.AlbumRequest;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<AlbumDto>> getAllAlbums() {
        List<AlbumDto> albumDtos = albumService.getAllAlbums();
        return ResponseEntity.ok(albumDtos);
    }

    @GetMapping("/artista")
    public ResponseEntity<List<AlbumDto>> getAllAlbumsByArtista(Long artistaId) {
        List<AlbumDto> album = albumService.getAlbumsByArtista();
        return ResponseEntity.ok(album);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @PostMapping
    public ResponseEntity<AlbumDto> crearAlbum(@RequestBody @Valid AlbumRequest albumRequest){
        AlbumDto albumDto = albumService.crearAlbum(albumRequest);
        return ResponseEntity.ok(albumDto);
    }

    @PostMapping("/{albumId}/canciones/{cancionId}")
    public ResponseEntity<List<CancionBasicDto>> agregarCancionAlbum(@PathVariable Long albumId, @PathVariable Long cancionId){
        List<CancionBasicDto> canciones = albumService.añadirCancionAlbum(cancionId,albumId );
        return ResponseEntity.ok(canciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDto> updateAlbum(@PathVariable Long id, @RequestBody AlbumRequest albumRequest) {
        return ResponseEntity.ok(albumService.updateAlbum(id, albumRequest));
    }

    @DeleteMapping("/{albumId}/canciones/{cancionId}")
    public ResponseEntity<List<CancionBasicDto>> eliminarCancionAlbum(@PathVariable Long albumId, @PathVariable Long cancionId){
        List<CancionBasicDto> canciones = albumService.eliminarCancionAlbum(albumId, cancionId);
        return ResponseEntity.ok(canciones);
    }


}
