package com.rebatosoft.minispotify.entities;

import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.entities.componentes.Follow;
import com.rebatosoft.minispotify.entities.componentes.Historial;
import com.rebatosoft.minispotify.entities.componentes.Playlist;
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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;
    private String contraseña;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuario",fetch = FetchType.EAGER)
    private List<Playlist> playlists;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuario",fetch = FetchType.LAZY)
    private List<Historial> cancionesEscuchadas;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuarioSeguidor",fetch = FetchType.LAZY)
    private List<Follow> follows;

    @OneToOne(mappedBy = "usuarioPropietario", cascade = CascadeType.ALL)
    private Artista datosArtista;
}
