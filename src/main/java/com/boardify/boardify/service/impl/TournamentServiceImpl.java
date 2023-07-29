package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.repository.TournamentRepository;
import com.boardify.boardify.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public void updateTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
    }

    @Override
    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    @Override
    public Optional<Tournament> findTournamentByID(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public List<Tournament> findAllTournamentsBeforeToday(Date today) {
        return tournamentRepository.findAllByEventEndBefore(today);
    }

    @Override
    public List<Tournament> findAllOpenTournaments(Date today) {
        return tournamentRepository.findAllOpenTournaments(today);
    }

    @Override
    public List<Tournament> findAllOpenTournamentsByUser(Date today, String email) {
        return tournamentRepository.findAllOpenTournamentsByUser(today,email);
    }

    @Override
    public Double findRating(Long tournamentId) {
        return tournamentRepository.findRating(tournamentId);
    }
    @Override
    public List<Tournament> findAllTournaments() {
        return tournamentRepository.findAll();
    }


}

