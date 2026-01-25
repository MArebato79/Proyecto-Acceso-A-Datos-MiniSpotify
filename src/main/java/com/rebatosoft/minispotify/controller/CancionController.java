package com.rebatosoft.minispotify.controller;

import com.rebatosoft.minispotify.dto.CancionDto;
import com.rebatosoft.minispotify.dto.ColaboracionDto;
import com.rebatosoft.minispotify.dto.requests.CancionRequest;
import com.rebatosoft.minispotify.service.CancionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CancionController {

    private final CancionService cancionService;

    // Metodos de Cancion

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

    @PutMapping("/{id}")
    public ResponseEntity<CancionDto> actualizarCancion(@PathVariable Long id, @RequestBody @Valid CancionRequest cancion) {
        CancionDto cancionEditada = cancionService.updateCancion(id,cancion);
        return ResponseEntity.ok(cancionEditada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCancion(@PathVariable Long id) {
        cancionService.deleteCancion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CancionDto>> obtenerCancionesBuscador(@RequestParam String filtro) {
        List<CancionDto> cancionesBuscadas = cancionService.searchCanciones(filtro);
        return ResponseEntity.ok(cancionesBuscadas);
    }

    // Metodos de Colaboraciones
    @PostMapping("/{id}/colaboradores")
    public ResponseEntity<List<ColaboracionDto>> anadirColaborador(@PathVariable("id") Long id, @RequestParam List<Integer> colaboracion) {
        List<ColaboracionDto> colaboracionDto = cancionService.agregarColaboradores(colaboracion,id.intValue());
        return ResponseEntity.ok(colaboracionDto);
    }


    @DeleteMapping("/{cancionId}/colaboradores/{id}")
    public ResponseEntity<List<ColaboracionDto>> eliminarColaborador(@PathVariable Long cancionId,@PathVariable Long id) {
        List<ColaboracionDto> lista = cancionService.eliminarColaborador(cancionId,id);
        return ResponseEntity.ok(lista);
    }




}
