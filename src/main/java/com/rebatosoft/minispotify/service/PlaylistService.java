package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import com.rebatosoft.minispotify.repositories.PlaylistRepository;
import com.rebatosoft.minispotify.repositories.TablasIntermedias.EntradaPlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class PlaylistService {

    private final CancionRepository cancionRepository;
    private final EntradaPlaylistRepository entradaPlaylistRepository;
    private final PlaylistRepository playlistRepository;
}
