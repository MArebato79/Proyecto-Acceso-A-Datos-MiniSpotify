package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
}
