package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.dto.PlaylistDto;
import com.rebatosoft.minispotify.entities.Usuario;
import com.rebatosoft.minispotify.entities.componentes.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Page<Playlist> findByUsuario(Usuario usuario, Pageable pageable);
    List<Playlist> findByPublicaTrue();
}
