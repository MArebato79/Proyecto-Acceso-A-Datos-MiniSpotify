package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.repositories.AlbumRepository;
import com.rebatosoft.minispotify.repositories.CancionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class AlbumService {

    private final AlbumRepository repository;
    private final CancionRepository cancionRepository;
}
