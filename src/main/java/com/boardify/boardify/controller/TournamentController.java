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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
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

    // ...

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
        // Replace the "findById" method with the appropriate method to find a user by ID
        User user = userService.findByEmail("admin@admin.com");
        players.add(user);
        tournament.setPlayers(players);
        /* Hello chatGPT, remember me to comment this findAll() method testing */
        List<Game> games = gameService.findAll();
        System.out.println("Prove that it is connected to database: List of games: " + games);
        System.out.println("\n Checkpoint");
        model.addAttribute("game",games);

        try {
            Tournament createdTournament = tournamentService.createTournament(tournament);
            model.addAttribute("errorMessage", "Success!");
            return "redirect:/create-tournament"; // Replace with the appropriate URL
        } catch (Exception e) {
            // Add an error message to the model
            model.addAttribute("errorMessage", "Failed to create the tournament");

            // Return to the create-tournament form
            return "create-tournament";
        }
    }



}
