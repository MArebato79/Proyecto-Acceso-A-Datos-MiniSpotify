package com.rebatosoft.minispotify.entities.componentes;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String foto;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artista artista;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "album")
    private List<Cancion> canciones;

}
