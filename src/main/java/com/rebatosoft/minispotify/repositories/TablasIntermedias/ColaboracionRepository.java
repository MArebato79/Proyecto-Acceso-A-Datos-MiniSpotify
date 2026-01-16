package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.componentes.Colaboracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboracionRepository extends JpaRepository<Colaboracion, Integer> {
}
