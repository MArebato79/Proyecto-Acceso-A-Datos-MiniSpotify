package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.componentes.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {
}
