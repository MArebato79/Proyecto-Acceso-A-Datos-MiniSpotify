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
public class PlaylistDto {
    private String id;
    private String nombre;
    private boolean publica;
    private String imagenUrl;
    private List<EntradaPlaylistDto> cancionesEntradas;
}
