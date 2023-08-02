package com.boardify.boardify.repository;

import com.boardify.boardify.entities.GameMechanics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameMechanicsRepository extends JpaRepository<GameMechanics, Long> {
}
