package com.boardify.boardify.repository;

import com.boardify.boardify.entities.TournamentPlayer;
import com.boardify.boardify.entities.TournamentPlayerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

@Repository
public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, TournamentPlayerKey> {
    @Query(value = "SELECT tp.* FROM tournament t " +
            "INNER JOIN tournament_players tp ON t.tournament_id = tp.tournament_id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') < :today AND tp.player_id = :playerId",
            nativeQuery = true)
    List<TournamentPlayer> findAllPastTournamentsByPlayer(Date today, Long playerId);

}
