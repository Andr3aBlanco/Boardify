package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.*;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.TournamentPlayerService;
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
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class TournamentController {
    private TournamentService tournamentService;
    private GameService gameService;
    private UserService userService;

    private TournamentPlayerService tournamentPlayerService;

    @Autowired
    public TournamentController(TournamentService tournamentService, GameService gameService,
                                UserService userService, TournamentPlayerService tournamentPlayerService) {
        this.tournamentService = tournamentService;
        this.gameService = gameService;
        this.userService = userService;
        this.tournamentPlayerService = tournamentPlayerService;
    }
//    @PostMapping("/tournaments/updateRating")
//    public String updateRatings(Long tournamentId, Long userId,Double ratingTournament,Double ratingHost)
//    {
//        Double rateTournament = tournamentService.findRating(tournamentId);
//        //Double rateHost = userService.findRating(userId);
//        Double newTournamentRating = ratingTournament * rateTournament;
//        //Double newHostRating = ratingHost * rateHost;
//        //tournamentService.updateRatingTournament(tournamentId,newTournamentRating);
//        //tournamentService.updateRatingHost(userId,newHostRating);
//
//        return "join-tournament";
//    }
    @GetMapping("/tournaments/join/{tournamentId}/null")
    public String handleNull(@PathVariable Long tournamentId)
    {
        return "redirect:../../../login";
    }
    @GetMapping("tournaments/join/{tournamentId}/{userId}")
    public RedirectView getTournament(@PathVariable Long tournamentId, @PathVariable Long userId, Model model) {
        System.out.println(tournamentId + " " + userId);
        try {
            // Check if userId is null
            if (userId == null) {
                // Redirect to the login page
                return new RedirectView("login");
            }

            // Check if the user is authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                // Redirect to the login page
                return new RedirectView("/login");
            }

            Optional<Tournament> tournament = tournamentService.findTournamentByID(tournamentId);

            if (tournament.isPresent()) {
                // Add the necessary data to the model
                model.addAttribute("tournament", tournament.get());
                model.addAttribute("userId", userId);
                TournamentPlayerKey tpKey = new TournamentPlayerKey(Long.valueOf(tournamentId), Long.valueOf(userId));

                tournamentPlayerService.addPlayerToTournament(tpKey);
                return new RedirectView("/dummy"); // change this for the URL you want to redirect
                // It is not going to show the button if it is full or it has ended
                // Every tournament you get will be ready to process
            } else {
                return new RedirectView("/join-tournament");
            }
        } catch (Exception e) {
            // Handle the exception here (log or show a friendly error message)
            e.printStackTrace();
            return new RedirectView("/");
        }
    }

    @GetMapping("/tournaments/edit/{id}")
    public String showEditTournamentPage(@PathVariable Long id, Model model) {
        Optional<Tournament> tournament = tournamentService.findTournamentByID(id);
        if (tournament.isPresent()) {
            model.addAttribute("tournament", tournament.get());
            List<Game> games = gameService.findAll();
            model.addAttribute("games", games);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);
            tournament.setOrganizer(user);
        }

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

        Game selectedGame = gameService.findGameById(gameId);
        tournament.setGame(selectedGame);

        // Add players to the tournament
//        List<User> players = new ArrayList<>();
//
//        // Get the user with ID 1 manually
//        // Replace the "findById" method with find by email
//        User user = userService.findByEmail("admin@admin.com");
//        players.add(user);
//        tournament.setPlayers(players);

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

    @PostMapping("/tournaments/update-rating/{tournamentId}")
    public String editRating(@PathVariable Long tournamentId, @ModelAttribute("ratingObj") TournamentPlayer ratingObj){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);
            if (user != null) {
                TournamentPlayerKey key = new TournamentPlayerKey(tournamentId, user.getId());
                Optional<TournamentPlayer> opTournamentPlayer = tournamentPlayerService.findTournamentPlayerByKey(key);
                if (!opTournamentPlayer.isEmpty()) {
                    TournamentPlayer tournamentPlayer = opTournamentPlayer.get();
                    tournamentPlayer.setTournamentRating(ratingObj.getTournamentRating());
                    tournamentPlayer.setOrganizerRating(ratingObj.getOrganizerRating());
                    tournamentPlayerService.savePlayerRating(tournamentPlayer);
                }
            }
        }

        return "redirect:/join-tournament";
    }

}
