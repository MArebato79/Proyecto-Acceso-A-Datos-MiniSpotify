package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.PlaylistDto;
import com.rebatosoft.minispotify.dto.basicsDto.PlaylistBasicDto;
import com.rebatosoft.minispotify.dto.requests.PlaylistRequest;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.service.CancionService;
import com.rebatosoft.minispotify.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlaylistController {

    final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistDto> crearPlaylist(@RequestBody @Valid PlaylistRequest playlistRequest) {
        PlaylistDto nuevaPlaylist = playlistService.crearPlaylist(playlistRequest);
        return ResponseEntity.ok(nuevaPlaylist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDto> updatePlaylist(@RequestBody @Valid PlaylistRequest playlistRequest, @PathVariable Long id) {
        PlaylistDto updatedPlaylist = playlistService.updatePlaylist(playlistRequest, id);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINTS PARA AGREGAR Y REMOVER CANCIONES DE LA PLAYLIST

    @PostMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<Void> agregarCancionAPlaylist(@PathVariable Long playlistId, @PathVariable Long cancionId  ) {
        playlistService.añadirCancionPlaylist(cancionId,playlistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<Void> removerCancionDePlaylist(@PathVariable Long playlistId, @PathVariable Long cancionId) {
        playlistService.eliminarCancionPlaylist(cancionId, playlistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<PlaylistBasicDto>> obtenerMyPlaylist() {
        List<PlaylistBasicDto> playlists = playlistService.getMyPlaylist();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> obtenerPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getPlaylistById(id));
    }
}
