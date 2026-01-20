package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.repositories.TablasIntermedias.FollowRepository;
import com.rebatosoft.minispotify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FollowRepository followRepository;

}
