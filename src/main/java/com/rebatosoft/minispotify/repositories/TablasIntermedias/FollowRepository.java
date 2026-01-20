package com.rebatosoft.minispotify.repositories.TablasIntermedias;

import com.rebatosoft.minispotify.entities.componentes.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByUsuarioSeguidorIdAndArtistaSeguidoId(Long uid, Long aid);
}
