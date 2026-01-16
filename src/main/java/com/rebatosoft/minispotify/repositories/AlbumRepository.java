package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
