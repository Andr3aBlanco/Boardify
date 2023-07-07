package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
