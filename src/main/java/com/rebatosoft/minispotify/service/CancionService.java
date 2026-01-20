package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.entities.componentes.Colaboracion;
import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.ArtistaRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.ColaboracionRepository;
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


}
