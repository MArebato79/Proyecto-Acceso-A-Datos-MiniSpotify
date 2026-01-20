package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancionDto {
    private Long id;
    private String titulo;
    private String genero;
    private ArtistaDto artista;
    private String colaboraciones;
    private AlbumBasicDto album;
}