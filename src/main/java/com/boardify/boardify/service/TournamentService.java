package com.boardify.boardify.service;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface TournamentService {

    List<Tournament> findAll();

    Tournament createTournament(Tournament tournament);

    void updateTournament(Tournament tournament);

    void deleteTournament(Long id);

    Optional<Tournament> findTournamentByID(Long id);

    List<Tournament> findTournamentsByPlayer(User user);

    List<Tournament> findAllExceptTournaments(List<Tournament> tournaments);

    List<Tournament> findFinishedTournaments();

}
