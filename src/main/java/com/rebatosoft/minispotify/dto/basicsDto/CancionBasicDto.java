package com.rebatosoft.minispotify.dto.basicsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancionBasicDto {
    private String id;
    private String titulo;
    private String imagenUrl;
    private String genero;
    private int duracion;
    private String artistaNombre;
    private List<ArtistaBasicDto> colaboradores;
}
