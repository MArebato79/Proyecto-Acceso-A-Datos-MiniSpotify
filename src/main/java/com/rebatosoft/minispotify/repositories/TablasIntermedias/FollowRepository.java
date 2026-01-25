package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.entities.componentes.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByUsuarioSeguidorIdAndArtistaSeguidoId(Long uid, Long aid);
    boolean existsByUsuarioSeguidorAndArtistaSeguido(Usuario usuario, Artista artista);
    Optional<Follow> findByUsuarioSeguidorAndArtistaSeguido(Usuario usuario, Artista artista);
    List<Follow> findByUsuarioSeguidor(Usuario usuario);
}
