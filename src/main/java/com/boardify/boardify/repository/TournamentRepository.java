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

    @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') < :today", nativeQuery = true)
    List<Tournament> findAllByEventEndBefore(Date today);
    /* @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d')< : today) AND organizerID = user_selected",nativeQuery = true);
     List<Tournament> findAllByEventEndBefore(Date today, Lond userID);*/
    @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') >= :today AND curr_enrolled < max_enrolled", nativeQuery = true)
    List<Tournament> findAllOpenTournaments(@Param("today") Date today);


    @Query(value = "SELECT t.* FROM tournament t " +
            "INNER JOIN tournament_players tp ON t.tournament_id = tp.tournament_id " +
            "INNER JOIN users u ON tp.user_id = u.user_id " +
            "WHERE STR_TO_DATE(t.event_end, '%Y-%m-%d') >= :today AND u.email = :email",
            nativeQuery = true)
    List<Tournament> findAllOpenTournamentsByUser(Date today, String email);

    @Query(value = "SELECT rating FROM tournament WHERE tournament_id = :tournamentId",nativeQuery = true)
    Double findRating(Long tournamentId);

}