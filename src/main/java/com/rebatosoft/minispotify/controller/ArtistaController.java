package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.ArtistaDto;
import com.rebatosoft.minispotify.dto.requests.ArtistaRequest;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService artistaService;

    @PostMapping()
    public ResponseEntity<ArtistaDto> createPerfilArtista(@RequestBody ArtistaRequest request) {
        ArtistaDto artistaDto = artistaService.createPerfilArtista(request);
        return ResponseEntity.ok(artistaDto);
    }

    @PutMapping()
    public ResponseEntity<ArtistaDto> updatePerfilArtista(@RequestBody ArtistaRequest request) {
        ArtistaDto artistaDto = artistaService.updateArtista(request);
        return ResponseEntity.ok(artistaDto);
    }

    //GETTERS

    @GetMapping
    public ResponseEntity<List<ArtistaDto>> getAllArtistas() {
        List<ArtistaDto> artistas = artistaService.getAllArtistas();
        return ResponseEntity.ok(artistas);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistaDto>> searchArtistas(@RequestParam("search") String search) {
        List<ArtistaDto> artistas = artistaService.searchArtista(search);
        return ResponseEntity.ok(artistas);
    }
}
