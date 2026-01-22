package com.rebatosoft.minispotify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboracionDto {
    private String id;
    private String artistaId;
    private String imagenUrl;
    private String nombre;
    private String rol;
}
