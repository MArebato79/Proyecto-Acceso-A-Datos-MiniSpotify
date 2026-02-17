package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancionDto {
    private Long id;
    private String titulo;
    private String imagenUrl;
    private String publica;
    private String genero;
    private ArtistaDto artista;
    private List<ColaboracionDto> colaboraciones;
    private AlbumBasicDto album;
}