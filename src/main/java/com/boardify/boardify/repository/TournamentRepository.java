package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT t.* FROM tournament t " +
            "INNER JOIN tournament_players tp ON t.tournament_id = tp.tournament_id " +
            "INNER JOIN users as u ON tp.player_id = u.id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') < :today AND u.id = :userId",
            nativeQuery = true)
    List<Tournament> findAllByEventEndBeforeAndUser(@Param("today") Date today, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') >= :today AND curr_enrolled < max_enrolled", nativeQuery = true)
    List<Tournament> findAllOpenTournaments(@Param("today") Date today);


    @Query(value = "SELECT t.* FROM tournament t " +
            "INNER JOIN users u ON t.organizer_id = u.id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') >= :today AND u.id = :id",
            nativeQuery = true)
    List<Tournament> findAllOpenTournamentsByUser(Date today, Long id);




    @Query(value = "SELECT rating FROM tournament WHERE tournament_id = :tournamentId",nativeQuery = true)
    Double findRating(Long tournamentId);

}