package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {
    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private ArtistaBasicDto perfilArtista;
}
