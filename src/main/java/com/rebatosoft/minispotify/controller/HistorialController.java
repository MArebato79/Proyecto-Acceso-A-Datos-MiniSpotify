package com.rebatosoft.minispotify.controller;


import com.rebatosoft.minispotify.entities.componentes.Historial;
import com.rebatosoft.minispotify.service.HistorialService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historial")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistorialController {

    private final HistorialService historialService;
    @PostMapping("/escuchar/{cancionId}")
    public ResponseEntity<Void> registrarEscucha(@PathVariable Long cancionId) {
        historialService.registrarEscucha(cancionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getMiHistorial(){
        return ResponseEntity.ok(historialService.getMiHistorial());
    }

}
