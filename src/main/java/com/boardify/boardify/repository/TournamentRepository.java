package com.boardify.boardify.repository;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') < :today", nativeQuery = true)
    List<Tournament> findAllByEventEndBefore(Date today);
   /* @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d')< : today) AND organizerID = user_selected",nativeQuery = true);
    List<Tournament> findAllByEventEndBefore(Date today, Lond userID);*/
   @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') >= :today", nativeQuery = true)
   List<Tournament> findAllOpenTournaments(Date today);

    @Query(value = "SELECT * FROM tournament WHERE STR_TO_DATE(event_end, '%Y-%m-%d') >= :today AND user_id = :userId", nativeQuery = true)
    List<Tournament> findAllOpenTournamentsByUser(Date today, Long userId);

}
