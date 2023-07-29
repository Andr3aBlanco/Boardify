package com.boardify.boardify.service;

import com.boardify.boardify.entities.Tournament;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface TournamentService {

    List<Tournament> findAll();

    Tournament createTournament(Tournament tournament);

    void updateTournament(Tournament tournament);

    void deleteTournament(Long id);

    Optional<Tournament> findTournamentByID(Long id);

    List<Tournament> findAllTournamentsBeforeToday(Date today);
    List<Tournament>findAllOpenTournaments(Date today);
    List<Tournament> findAllOpenTournamentsByUser(Date today, String email);
    Double findRating(Long tournamentId);
//    List<Tournament> findAllOpenTournamentsByUser(Date today, Long userId);

    List<Tournament> findAllTournaments();
}
