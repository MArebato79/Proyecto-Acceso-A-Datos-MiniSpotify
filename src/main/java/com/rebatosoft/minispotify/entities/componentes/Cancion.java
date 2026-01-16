package com.rebatosoft.minispotify.entities.componentes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;

    private GENEROS genero;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Artista autor;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "cancion")
    private List<Colaboracion> colaboraciones ;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "cancionEscuchada")
    private List<Historial> escuchasDeLaCancion;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Album album;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "",fetch = FetchType.LAZY)
    private List<EntradaPlaylist> entradaPlaylists;

}
