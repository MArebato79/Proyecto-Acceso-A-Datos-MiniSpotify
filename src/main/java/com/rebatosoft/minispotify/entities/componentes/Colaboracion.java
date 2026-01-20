package com.rebatosoft.minispotify.entities.componentes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Colaboracion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artista artistaColaborador;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cancion cancion;

    private String rol;
}
