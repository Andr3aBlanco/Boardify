package com.boardify.boardify.controller;

import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TournamentController {

    private TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/join")
    public String joinTournament(Model model) {
        List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);
        return "redirect:/tournaments";
    }
}