package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.entities.componentes.Historial;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.HistorialRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialService {

    private final HistorialRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;

    @Transactional
    public void registrarEscucha(Long cancionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new RuntimeException("Canción no existe"));

        Historial historial = new Historial();
        historial.setUsuario(usuario);
        historial.setCancionEscuchada(cancion);
        historial.setTiempoEscucha(LocalDateTime.now());

        historialRepository.save(historial);
    }

    public List<HistorialItemDto> getMiHistorial() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseThrow();

        return historialRepository.findByUsuario(usuario).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }


    @Getter @Setter
    public static class HistorialItemDto {
        private CancionBasicDto cancion;
        private String fecha;
    }

    private HistorialItemDto convertirADto(Historial h) {
        HistorialItemDto dto = new HistorialItemDto();
        dto.setFecha(h.getTiempoEscucha().toString());

        if (h.getCancionEscuchada() != null) {
            CancionBasicDto cBasic = new CancionBasicDto();
            cBasic.setId(String.valueOf(h.getCancionEscuchada().getId()));
            cBasic.setTitulo(h.getCancionEscuchada().getTitulo());
            dto.setCancion(cBasic);
        }
        return dto;
    }
}
