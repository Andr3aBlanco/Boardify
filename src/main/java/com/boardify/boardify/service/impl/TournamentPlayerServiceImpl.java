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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentPlayerServiceImpl implements TournamentPlayerService {
    TournamentPlayerRepository tournamentPlayerRepository;
    TournamentRepository tournamentRepository;
    UserRepository userRepository;


    @Autowired
    private final EntityManagerFactory emf = null; // to facilitate queries to database that output group by values


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


    public Optional<TournamentPlayer> findTournamentPlayerByKey(TournamentPlayerKey key) {
        return tournamentPlayerRepository.findById(key);
    }

    public List<Object[]> findJoinedTournamentsCountPerPlayer() { // to get players' stats for leaderboard.html
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createQuery(
                    "SELECT tp.player.username, COUNT(tp.tournament.tournamentId) AS numAttended " +
                            "FROM TournamentPlayer tp " +
                            "GROUP BY tp.player.id " +
                            "ORDER BY numAttended DESC " +
                            "FETCH FIRST 10 ROWS ONLY");
        return query.getResultList();
    }

    public List<Object[]> findOrganizerStats() { // to get for organizers' stats for leaderboard.html
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createQuery(
                    "SELECT t.organizer.username, ROUND(AVG(tp.organizerRating), 2), ROUND(AVG(tp.tournamentRating),2), COUNT(DISTINCT t.tournamentId) " +
                            "FROM TournamentPlayer tp JOIN Tournament t ON t.tournamentId = tp.tournament.tournamentId " +
                            "WHERE tp.organizerRating > -1 AND tp.tournamentRating > -1 " +
                            "GROUP BY t.organizer " +
                            "ORDER BY ROUND(AVG(tp.organizerRating),2) DESC " +
                            "FETCH FIRST 10 ROWS ONLY");
        return query.getResultList();
    }

    public List<TournamentPlayer> findAllTournamentPlayers() {
        return tournamentPlayerRepository.findAll();
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
