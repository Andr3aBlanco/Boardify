package com.boardify.boardify.controller;

import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.TournamentService;
import com.boardify.boardify.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TournamentController {
    private TournamentService tournamentService;
    private GameService gameService;
    @Autowired
    private UserService userService;
    // Inside the TournamentController class
    private List<Tournament> userEnrolledTournaments;


    @Autowired
    public TournamentController(TournamentService tournamentService, GameService gameService, UserService userService) {
        this.tournamentService = tournamentService;
        this.gameService = gameService;
        this.userService = userService;


    }//added join,rate  and cancel tournament
    @GetMapping("/join-tournament")
    public String joinTournamentPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Retrieve the currently logged-in user
        User user = userService.findByEmail(currentPrincipalName);

        // Retrieve the list of tournaments where the user is enrolled
        List<Tournament> userEnrolledTournaments = tournamentService.findTournamentsByPlayer(user);
        model.addAttribute("userEnrolledTournaments", userEnrolledTournaments);

        // Retrieve all tournaments except the ones where the user is already enrolled
        List<Tournament> allTournamentsExceptEnrolled = tournamentService.findAllExceptTournaments(userEnrolledTournaments);
        model.addAttribute("tournaments", allTournamentsExceptEnrolled);
        // Retrieve the list of finished tournaments (event_end before today)
        List<Tournament> finishedTournaments = tournamentService.findFinishedTournaments();
        model.addAttribute("finishedTournaments", finishedTournaments);


        // ... Other model attributes ...

        return "join-tournament";
    }
    @PostMapping("/tournaments/rate/{id}")
    public String rateTournament(@PathVariable Long id, @RequestParam("userRating") int userRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Retrieve the currently logged-in user
        User user = userService.findByEmail(currentPrincipalName);

        Optional<Tournament> optionalTournament = tournamentService.findTournamentByID(id);
        if (optionalTournament.isPresent()) {
            Tournament tournament = optionalTournament.get();
            // Check if the tournament is a finished tournament
            if (tournament.getEventEnd().compareTo(LocalDate.now()) <= 0) {
                // Set the user's rating for the tournament
                tournament.setUserRating(userRating);
                // Save the updated tournament
                tournamentService.updateTournament(tournament);
            }
        }
        return "redirect:/join-tournament";
    }


    @PostMapping("/tournaments/cancel/{id}")
    public String cancelTournamentEnrollment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Retrieve the currently logged-in user
        User user = userService.findByEmail(currentPrincipalName);

        Optional<Tournament> optionalTournament = tournamentService.findTournamentByID(id);
        if (optionalTournament.isPresent()) {
            Tournament tournament = optionalTournament.get();
            // Check if the tournament is in the user's enrolled tournaments
            if (tournament.getPlayers().contains(user)) {
                // Remove the user from the tournament's players list
                tournament.getPlayers().remove(user);
                // Decrement the currentEnrolled count
                tournament.setCurrEnrolled(tournament.getCurrEnrolled() - 1);
                // Save the updated tournament
                tournamentService.updateTournament(tournament);
            }
        }
        return "redirect:/join-tournament";
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
    @GetMapping("/tournaments/delete/{id}")
    public String deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);

        return "redirect:/tournament-to-edit";
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
