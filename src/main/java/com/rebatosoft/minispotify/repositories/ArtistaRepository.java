package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
}
