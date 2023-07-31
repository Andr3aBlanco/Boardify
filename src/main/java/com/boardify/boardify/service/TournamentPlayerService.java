package com.boardify.boardify.service;

import com.boardify.boardify.entities.TournamentPlayer;
import com.boardify.boardify.entities.TournamentPlayerKey;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface TournamentPlayerService {
    void addPlayerToTournament(TournamentPlayerKey key);

    List<TournamentPlayer> findAllPastTournamentsByPlayer(Date dateToday, Long playerId);

    void savePlayerRating(TournamentPlayer tournamentRatingByPlayer);

    Optional<TournamentPlayer> findTournamentPlayerByKey(TournamentPlayerKey key);

    List<Object[]> findJoinedTournamentsCountPerPlayer();

    List<Object[]> findOrganizerStats();

}
