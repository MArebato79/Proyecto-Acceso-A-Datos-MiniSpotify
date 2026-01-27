package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.dto.AlbumDto;
import com.rebatosoft.minispotify.entities.componentes.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    List<Album> findAllByArtistaId(Long artistaId);
}
