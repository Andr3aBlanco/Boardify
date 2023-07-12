package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g.apiId FROM Game g")
    Set<String> findAllApiIds();
}
