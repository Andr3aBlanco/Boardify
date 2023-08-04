package com.boardify.boardify.repository;


import com.boardify.boardify.entities.Tournament;
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
            "INNER JOIN users as u ON tp.player_id = u.id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') < :today AND u.email = :email",
            nativeQuery = true)
    List<TournamentPlayer> findAllPastTournamentsByPlayer(Date today, String email);
    //I need this one Chris
    @Query(value = "SELECT tp.* FROM tournament_players tp " +
            "INNER JOIN tournament t ON t.tournament_id = tp.tournament_id " +
            "INNER JOIN users u ON tp.player_id = u.id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') >= :today AND u.id = :userId",
            nativeQuery = true)
    List<TournamentPlayer> findJoinedTournamentsByPlayer(Date today, Long userId);

}
