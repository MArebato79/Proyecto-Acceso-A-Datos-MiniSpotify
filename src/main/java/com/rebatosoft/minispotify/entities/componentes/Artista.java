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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String biografia;

    private String foto;

    private int numeroCanciones;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "artista")
    private List<Album> albums;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "artistaColaborador", fetch = FetchType.LAZY)
    private List<Colaboracion> colaboraciones;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "autor",fetch = FetchType.LAZY)
    private List<Cancion> canciones;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "artistaSeguido",fetch = FetchType.LAZY)
    private List<Follow> follows;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuarioPropietario;
}
