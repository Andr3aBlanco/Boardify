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
        if (tournamentFind.isPresent() && userFind.isPresent()) {
            //Optional.isPresent() and Optional.get() is safer than using !Optional.isEmpty() and Optional.get(),
            // as the latter can lead to NoSuchElementException if the Optional is empty.
            Tournament tournament = tournamentFind.get();
            int currentEnrolled = tournament.getCurrEnrolled();
            tournament.setCurrEnrolled(currentEnrolled + 1);

            TournamentPlayer tournamentPlayer = new TournamentPlayer();
            tournamentPlayer.setTournament(tournament);
            tournamentPlayer.setPlayer(userFind.get());
            tournamentPlayer.setId(key);

            tournamentRepository.save(tournament);
            tournamentPlayerRepository.save(tournamentPlayer);
        }
    }


    public List<TournamentPlayer> findAllPastTournamentsByPlayer(Date dateToday, String email) {
        return this.tournamentPlayerRepository.findAllPastTournamentsByPlayer(dateToday, email);
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
    public List<TournamentPlayer> findJoinedTournamentsByPlayer(Date dateToday, Long userId) {
        return this.tournamentPlayerRepository.findJoinedTournamentsByPlayer(dateToday, userId);
    }
    public void cancelEnrollment(TournamentPlayer tournamentPlayer) {
        Tournament tournament = tournamentPlayer.getTournament();
        if (tournament.getCurrEnrolled() > 0) {
            tournament.setCurrEnrolled(tournament.getCurrEnrolled() - 1);
        }
        tournament.getTournamentPlayers().remove(tournamentPlayer);
        tournamentPlayerRepository.delete(tournamentPlayer);
        tournamentRepository.save(tournament);
    }

}
