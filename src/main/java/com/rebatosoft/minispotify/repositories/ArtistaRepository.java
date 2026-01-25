package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    List<Artista> findByNombreContainingIgnoreCase(String termino);
    List<Artista> findAllById(List<Integer> colaboradoresIds);
}
