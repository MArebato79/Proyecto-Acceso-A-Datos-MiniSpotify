package com.rebatosoft.minispotify.entities.componentes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EntradaPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaIntroduccion;

    private int posicionCancion;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Playlist playlist;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Cancion cancion;
}
