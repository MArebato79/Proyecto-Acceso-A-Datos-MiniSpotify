package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.componentes.Cancion;
import com.rebatosoft.minispotify.entities.componentes.EntradaPlaylist;
import com.rebatosoft.minispotify.entities.componentes.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaPlaylistRepository extends JpaRepository<EntradaPlaylist, Integer> {
    EntradaPlaylist findByCancionId(int cancionId);
    EntradaPlaylist findByCancionAndPlaylist(Cancion cancion, Playlist playlist);
}
