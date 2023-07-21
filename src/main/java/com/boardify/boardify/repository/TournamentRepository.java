package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findAllByPlayersContaining(User user);
    // Add the new method for finding all tournaments except the given list of tournaments
    List<Tournament> findAllByTournamentIdNotIn(List<Long> tournamentIds);

    List<Tournament> findByEventEndBefore(LocalDate date);
}
