package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Add custom query methods if needed
}
