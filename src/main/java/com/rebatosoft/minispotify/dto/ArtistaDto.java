package com.rebatosoft.minispotify.dto;

import com.rebatosoft.minispotify.dto.basicsDto.AlbumBasicDto;
import com.rebatosoft.minispotify.dto.basicsDto.PlaylistBasicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaDto {
    private String id;
    private String nombre;
    private List<PlaylistBasicDto> playlists;
    private List<AlbumBasicDto> albums;
}
