package com.boardify.boardify.controller;

import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.TournamentService;
import com.boardify.boardify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TournamentController {
    private TournamentService tournamentService;
    private GameService gameService;
    private UserService userService;

    @Autowired
    public TournamentController(TournamentService tournamentService, GameService gameService, UserService userService) {
        this.tournamentService = tournamentService;
        this.gameService = gameService;
        this.userService = userService;


    }



    @GetMapping("/tournaments/edit/{id}")
    public String showEditTournamentPage(@PathVariable Long id, Model model) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(id);
        if (tournament.isPresent()) {
            model.addAttribute("tournament", tournament.get());
            return "edit-tournament";
        }
        return "redirect:/tournament-to-edit";
    }

    // Save the edited tournament details
    @PostMapping("/tournaments/edit")
    public String editTournament(@ModelAttribute @Valid Tournament tournament, BindingResult bindingResult,
                                 @RequestParam("gameId") Long gameId, Model model) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors and return to the edit-tournament form
            return "edit-tournament";
        }

        // Get the selected game and set it in the tournament
        Game selectedGame = gameService.findGameById(gameId);
        tournament.setGame(selectedGame);

        // Save the edited tournament
        tournamentService.updateTournament(tournament);

        return "redirect:/tournament-to-edit";
    }

    // Delete the selected tournament
    @PostMapping("/tournaments/delete/{id}")
    public String deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);

        return "redirect:/join-tournament";
    }

    @PostMapping("/tournaments/add")
    public String createTournament(@ModelAttribute @Valid Tournament tournament, BindingResult bindingResult,
                                   @RequestParam("gameId") Long gameId, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the create-tournament form
            System.out.println("\n Checkpoint");
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println("Error in field: " + error.getField());
                System.out.println("Error message: " + error.getDefaultMessage());
            }
            return "create-tournament";
        }

        // Set the organizer ID to 1 manually
        tournament.setOrganizerId(1L);
        Game selectedGame = gameService.findGameById(gameId);
        tournament.setGame(selectedGame);

        // Add players to the tournament
        List<User> players = new ArrayList<>();

        // Get the user with ID 1 manually
        // Replace the "findById" method with find by email
        User user = userService.findByEmail("admin@admin.com");
        players.add(user);
        tournament.setPlayers(players);

        List<Game> tournamentGames = new ArrayList<>();
        tournamentGames.add(selectedGame);
        tournament.setGames(tournamentGames);



        try {
            Tournament createdTournament = tournamentService.createTournament(tournament);
            model.addAttribute("errorMessage", "Success!");




            return "redirect:/"; // Replace with the appropriate URL
        } catch (Exception e) {
            // Add an error message to the model
            model.addAttribute("errorMessage", "Failed to create the tournament");

            // Return to the create-tournament form
            return "create-tournament";
        }
    }



}
