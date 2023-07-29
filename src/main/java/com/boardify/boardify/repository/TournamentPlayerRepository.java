package com.boardify.boardify.repository;

import com.boardify.boardify.entities.TournamentPlayer;
import com.boardify.boardify.entities.TournamentPlayerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, TournamentPlayerKey> {


}
