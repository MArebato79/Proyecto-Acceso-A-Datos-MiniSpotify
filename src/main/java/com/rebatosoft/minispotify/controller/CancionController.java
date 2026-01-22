package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.requests.CancionRequest;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.service.CancionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/cancion")
@RequiredArgsConstructor
public class CancionController {

    private final CancionService cancionService;

    @PostMapping()
    public ResponseEntity<CancionDto> crearCancion(@RequestBody @Valid CancionRequest cancion) {
        CancionDto cancionNueva =  cancionService.crearCancion(cancion);
        return new ResponseEntity<>(cancionNueva,HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<CancionDto> actualizarEstadoCancion(@PathVariable Long id,
                                                              @RequestParam boolean estado) {
        CancionDto cancionEditada = cancionService.cambiarEstado(estado,id);
        return ResponseEntity.ok(cancionEditada);
    }

}
