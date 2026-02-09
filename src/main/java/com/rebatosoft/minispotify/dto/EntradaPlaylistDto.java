package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.ArtistaBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.CancionBasicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntradaPlaylistDto {
    private String id;
    private Integer posicion;
    private CancionBasicDto cancion;
    private ArtistaBasicDto artista;
    private String fechaAgregado;
}
