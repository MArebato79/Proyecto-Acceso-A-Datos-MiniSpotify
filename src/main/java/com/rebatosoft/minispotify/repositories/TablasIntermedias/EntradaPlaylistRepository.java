package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.componentes.EntradaPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaPlaylistRepository extends JpaRepository<EntradaPlaylist, Integer> {
}
