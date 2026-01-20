package com.rebatosoft.minispotify.entities.componentes;

import com.rebatosoft.minispotify.entities.Usuario;
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
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String foto;

    private int numeroCanciones;

    private boolean publica;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<EntradaPlaylist> cancionesEntradas;
}
