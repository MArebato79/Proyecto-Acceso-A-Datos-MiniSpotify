package com.rebatosoft.minispotify.dto.basicsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistBasicDto {
    private String id;
    private String nombre;
    private String imagenUrl;
}
