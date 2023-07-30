package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.TournamentPlayerKey;
import com.boardify.boardify.entities.TournamentPlayer;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.TournamentPlayerRepository;
import com.boardify.boardify.repository.TournamentRepository;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.TournamentPlayerService;
import com.boardify.boardify.service.TournamentService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentPlayerServiceImpl implements TournamentPlayerService {
    TournamentPlayerRepository tournamentPlayerRepository;
    TournamentRepository tournamentRepository;
    UserRepository userRepository;

    public TournamentPlayerServiceImpl(TournamentPlayerRepository TPRepository, TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentPlayerRepository = TPRepository;
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
    }
    public void addPlayerToTournament(TournamentPlayerKey key) {
        Optional<Tournament> tournamentFind = tournamentRepository.findById(key.getTournamentId());
        Optional<User> userFind = userRepository.findById(key.getPlayerId());
        if (!tournamentFind.isEmpty() && !userFind.isEmpty()) {
            TournamentPlayer tournamentPlayer = new TournamentPlayer();
            tournamentPlayer.setTournament(tournamentFind.get());
            tournamentPlayer.setPlayer(userFind.get());
            tournamentPlayer.setId(key);
            tournamentPlayerRepository.save(tournamentPlayer);
        }
    }

    public List<TournamentPlayer> findAllPastTournamentsByPlayer(Date dateToday, Long playerId) {
        return this.tournamentPlayerRepository.findAllPastTournamentsByPlayer(dateToday, playerId);
    }

    public void savePlayerRating(TournamentPlayer tournamentRatingByPlayer) {
        tournamentPlayerRepository.save(tournamentRatingByPlayer);
    }

//    public Optional<TournamentPlayer> findTournamentPlayerByKey(TournamentPlayerKey key) {
//        return tournamentPlayerRepository.findById(key);
//    }

        public Optional<TournamentPlayer> findTournamentPlayerByKey(TournamentPlayerKey key) {
            return tournamentPlayerRepository.findById(key);
    }
}
