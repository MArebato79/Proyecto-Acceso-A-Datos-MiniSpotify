package com.rebatosoft.minispotify.entities.componentes;

import com.rebatosoft.minispotify.entities.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate fecha;

    @ManyToOne(fetch =  FetchType.EAGER)
    private Usuario usuarioSeguidor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Artista artistaSeguido;
}
