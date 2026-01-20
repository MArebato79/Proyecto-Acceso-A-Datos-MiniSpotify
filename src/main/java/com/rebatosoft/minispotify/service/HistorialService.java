package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.HistorialRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class HistorialService {

    private final HistorialRepository historialRepository;
    private final CancionRepository cancionRepository;
    private final UsuarioRepository usuarioRepository;
}
