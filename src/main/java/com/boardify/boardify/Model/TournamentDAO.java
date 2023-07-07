package com.boardify.boardify.Model;

import com.boardify.boardify.entities.Tournament;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TournamentDAO {
    private final JdbcTemplate jdbcTemplate;

    public TournamentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAllTournaments() {
        return jdbcTemplate.queryForList("SELECT * FROM Tournaments");
    }

    public int createTournament(Tournament tournament) {
        return jdbcTemplate.update(
                "INSERT INTO Tournaments (tournament_name, address, city, state, status) VALUES (?, ?, ?, ?, ?)",
                tournament.getTournamentName(), tournament.getAddress(), tournament.getCity(), tournament.getState(), tournament.getStatus()
        );
    }

    public int updateTournament(Tournament tournament) {
        return jdbcTemplate.update(
                "UPDATE Tournaments SET tournament_name = ?, address = ?, city = ?, state = ?, status = ? WHERE tournament_ID = ?",
                tournament.getTournamentName(), tournament.getAddress(), tournament.getCity(), tournament.getState(), tournament.getStatus(), tournament.getTournamentID()
        );
    }

    public int deleteTournament(Long tournamentId) {
        return jdbcTemplate.update("DELETE FROM Tournaments WHERE tournament_ID = ?", tournamentId);
    }
}
