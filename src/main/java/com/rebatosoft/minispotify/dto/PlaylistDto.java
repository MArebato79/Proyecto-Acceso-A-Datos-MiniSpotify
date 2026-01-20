package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.EntradaPlaylist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto {
    private String id;
    private String nombre;
    private String imagenUrl;
    private List<EntradaPlaylistDto> cancionesEntradas;
}
