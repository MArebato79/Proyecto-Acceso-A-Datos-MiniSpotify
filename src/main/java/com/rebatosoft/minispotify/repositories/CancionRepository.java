package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Cancion;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    Page<Cancion> findByTituloContainingIgnoreCase(String termino, Pageable pageable);
    List<Cancion> findByAutorId(Long idArtista);
}
