package com.rebatosoft.minispotify.service;

import com.rebatosoft.minispotify.dto.UsuarioDto;
import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.requests.RegisterRequest;
import com.rebatosoft.minispotify.entities.Usuario;
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



    private UsuarioDto convertirADto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId().toString());
        dto.setUsername(usuario.getCorreo());
        dto.setEmail(usuario.getCorreo());

        if (usuario.getDatosArtista() != null) {
            ArtistaBasicDto artDto = new ArtistaBasicDto();
            artDto.setId(usuario.getDatosArtista().getId().toString());
            artDto.setNombre(usuario.getDatosArtista().getNombre());
            dto.setPerfilArtista(artDto);
        }
        return dto;
    }

    // Para el registro inicial
    private Usuario convertirAEntidad(RegisterRequest request, String passwordCodificada) {
        Usuario u = new Usuario();
        u.setCorreo(request.email());
        u.setContraseña(passwordCodificada);
        return u;
    }
}
