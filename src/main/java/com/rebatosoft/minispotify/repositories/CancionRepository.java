package com.rebatosoft.minispotify.repositories;

import com.rebatosoft.minispotify.entities.componentes.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
}
