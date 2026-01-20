package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {
    private String id;
    private String nombre;
    private String imagenUrl;
    private ArtistaBasicDto artista;
    private List<CancionBasicDto> canciones;
}
