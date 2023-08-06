package com.boardify.boardify.controller;





import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;

import com.boardify.boardify.entities.*;
import com.boardify.boardify.DTO.UserDto;

import com.boardify.boardify.repository.RoleRepository;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.*;



import jakarta.servlet.RequestDispatcher;


import com.boardify.boardify.entities.Tournament;
import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.text.SimpleDateFormat;

import java.util.*;

import java.util.Date;


import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class CoreController implements ErrorController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired

    private TournamentService tournamentService;

    @Autowired
    private GameService gameService;

    public CoreController(UserRepository userRepository) {
        // this.userRepository = userRepository;
    }

    @Autowired
    private BoardGameAtlasService atlasService;






    @Autowired
    private TournamentPlayerService tournamentPlayerService;


    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Tournament> tournaments = tournamentService.findAllTournaments();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);

            if (user != null) {
                String username = user.getUsername();
                // Add the necessary data to the model
                model.addAttribute("username", username);
                model.addAttribute("message", "Hello " + username + "!");
                model.addAttribute("tournaments", tournaments);
            }



        }

        BoardGameResponse allGames = atlasService.retrieveFour();
        List<GameSearchResult> games = allGames.getGames();
        model.addAttribute("gamesList", games);


        // Manually add request as a context variable
        model.addAttribute("request", request);
        model.addAttribute("tournaments", tournaments);
        // Return the name of the Thymeleaf template for the home page
        return "home";
    }



    @GetMapping("/join-tournament")
    public String showJoinTournamentPage(Model model, HttpServletRequest request) {
        Date today = new Date();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);

            if (user != null) {
                String username = user.getUsername();

                // Add the necessary data to the model
                model.addAttribute("username", username);

                String role = user.getRoles().get(0).getName();


                if (user.getId() != null)
                {
                    Long myId = user.getId();
                    model.addAttribute("userId",myId);

                    if (role.equals("ROLE_ADMIN"))
                    {
                        List<TournamentPlayer> joinedTournaments = tournamentPlayerService.findJoinedTournamentsByPlayer(today, user.getId());
                        model.addAttribute("joinedTournaments", joinedTournaments);
                        List<Tournament> openTournaments = tournamentService.findAllOpenTournaments(today);//Show all tournaments
                        model.addAttribute("myTournaments",openTournaments);
                        List<TournamentPlayer> pastTournaments = tournamentPlayerService.findAllPastTournamentsByPlayer(today, user.getEmail());
                        model.addAttribute("pastTournaments", pastTournaments);

                    }
                    else
                    {
                        List<Tournament> myTournaments = tournamentService.findAllOpenTournamentsByUser(today,user.getId());
                        model.addAttribute("myTournaments",myTournaments);//Show tournaments created by logged in user ONLY
                        List<TournamentPlayer> pastTournaments = tournamentPlayerService.findAllPastTournamentsByPlayer(today, user.getEmail());
                        model.addAttribute("pastTournaments", pastTournaments);
                        List<TournamentPlayer> joinedTournaments = tournamentPlayerService.findJoinedTournamentsByPlayer(today, user.getId());
                        model.addAttribute("joinedTournaments", joinedTournaments);
                    }

                }
            }
        }

        List<Tournament> openTournaments = tournamentService.findAllOpenTournaments(today);
        model.addAttribute("openTournaments",openTournaments);


        TournamentPlayer ratingObj = new TournamentPlayer();
        model.addAttribute("ratingObj", ratingObj);


        return "join-tournament";
    }
    @GetMapping("/edit-tournament")
    public String showEditTournamentPage(Model model, HttpServletRequest request) {

        List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games);

        return "tournament-to-edit";
    }

    @GetMapping("/create-tournament")
    public String showCreateTournamentPage(Model model, HttpServletRequest request) {
//         Add necessary logic or data retrieval here
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);
            if (user != null) {

                System.out.println(user.getRoles() + " " + user.getRoles().size());
                String role = user.getRoles().get(0).getName();
                if (role.equals("ROLE_BASIC")) {
                    return "redirect:/go-premium";
                } else if (role.equals("ROLE_PREMIUM") || role.equals("ROLE_ADMIN")) {

                    model.addAttribute("request", request);
                    Tournament tournament = new Tournament();
                    model.addAttribute("tournament", tournament);
                    List<Game> games = gameService.findAll();
                    model.addAttribute("games", games);
                    return "create-tournament";
               }
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
        return "redirect:/login";
    }


    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {

        ArrayList<String> playersUsernames = new ArrayList<>();
        ArrayList<Long> attendanceCounts = new ArrayList<>();
        List<Object[]> playersAttendanceCount = tournamentPlayerService.findJoinedTournamentsCountPerPlayer();
        for (Object[] playerAttendance : playersAttendanceCount) {
            playersUsernames.add((String) playerAttendance[0]);
            attendanceCounts.add((Long) playerAttendance[1]);
        }

        model.addAttribute("players", playersUsernames);
        model.addAttribute("attendanceCounts", attendanceCounts);

        ArrayList<String> organizerUsernames = new ArrayList<>();
        ArrayList<Long> tournamentCounts = new ArrayList<>();
        ArrayList<Double> avgOrganizerRatings = new ArrayList<>();
        ArrayList<Double> avgTournamentRatingsPerOrganizer = new ArrayList<>();
        List<Object[]> organizersStatistics = tournamentPlayerService.findOrganizerStats();
        for (Object[] organizerStats : organizersStatistics) {
            organizerUsernames.add((String) organizerStats[0]);
            avgOrganizerRatings.add((Double) organizerStats[1]);
            avgTournamentRatingsPerOrganizer.add((Double) organizerStats[2]);
            tournamentCounts.add((Long) organizerStats[3]);
        }
        model.addAttribute("organizers", organizerUsernames);
        model.addAttribute("numHosted", tournamentCounts);
        model.addAttribute("organizerRating", avgOrganizerRatings);
        model.addAttribute("tournamentsRating", avgTournamentRatingsPerOrganizer);


        return "leaderboard";
    }



    private UserService userService;

    @Autowired
    public CoreController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User currentUser = userService.findByEmail(email);
            if (currentUser != null) {
                UserDto userDto = new UserDto();
                userDto.setUsername(currentUser.getUsername());
                userDto.setFirstName(currentUser.getFirstName());
                return userDto;
            }
        }
        return null;
    }


}

