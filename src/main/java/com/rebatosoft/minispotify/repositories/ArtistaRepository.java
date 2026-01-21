package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Artista;
import com.rebatosoft.minispotify.entities.componentes.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
    Artista findById(Long id);
    List<Artista> findByNombreContainingIgnoreCase(String termino);
}
