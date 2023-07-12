package com.boardify.boardify.controller;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tournaments")
public class TournamentRestController {

    private TournamentService tournamentService;

    @Autowired
    public TournamentRestController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/join")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.findAll();
        return ResponseEntity.ok(tournaments);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(id);
        if (tournament.isPresent()) {
            return ResponseEntity.ok(tournament.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        Tournament createdTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTournament);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        Optional<Tournament> existingTournament = tournamentService.findTournamentByID(id);
        if (existingTournament.isPresent()) {
            tournament.setTournamentID(id);
            tournamentService.updateTournament(tournament);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        Optional<Tournament> existingTournament = tournamentService.findTournamentByID(id);
        if (existingTournament.isPresent()) {
            tournamentService.deleteTournament(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
