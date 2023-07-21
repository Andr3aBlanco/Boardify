package com.boardify.boardify.controller;



import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.GameService;
import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class CoreController implements ErrorController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TournamentService tournamentService;

    private final UserRepository userRepository;
    @Autowired

    private GameService gameService;

    public CoreController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Inside the TournamentController class
    private List<Tournament> userEnrolledTournaments;


    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                String username = user.getUsername();
                // Add the necessary data to the model
                model.addAttribute("username", username);
                model.addAttribute("message", "Hello " + username + "!");
            }
        }

        // Manually add request as a context variable
        model.addAttribute("request", request);

        // Return the name of the Thymeleaf template for the home page
        return "home";
    }


/*
    @GetMapping("/edit-tournament")
    public String showEditTournamentPage(Model model, HttpServletRequest request)
    {
        List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);

        return "tournament-to-edit";
    }
*/



    @GetMapping("/join-tournament")
    public String showJoinTournamentPage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                // Add the currently logged-in user ID to the model
                model.addAttribute("loggedInUserId", user.getId());
                // Retrieve the list of tournaments where the user is enrolled
                List<Tournament> userEnrolledTournaments = tournamentService.findTournamentsByPlayer(user);
                model.addAttribute("userEnrolledTournaments", userEnrolledTournaments);
            }
        }

        // Retrieve all tournaments except the ones where the user is already enrolled
        List<Tournament> allTournamentsExceptEnrolled = tournamentService.findAllExceptTournaments(userEnrolledTournaments);
        model.addAttribute("tournaments", allTournamentsExceptEnrolled);

        // Retrieve the list of finished tournaments (event_end before today)
        List<Tournament> finishedTournaments = tournamentService.findFinishedTournaments();
        model.addAttribute("finishedTournaments", finishedTournaments);

        // ... Other model attributes ...

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
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);
        Tournament tournament = new Tournament();
        model.addAttribute("tournament", tournament);
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games);
        return "create-tournament";
    }


    @GetMapping("/leaderboard")
    public String showLeaderboardPage(Model model, HttpServletRequest request) {
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);

        return "leaderboard";
    }

//    @GetMapping("/error")
//    public String handleError() {
//        // Handle the error and provide a custom error page or redirect
//        return "redirect:/home"; // Replace "error" with the appropriate template name or redirect path
//    }


    @GetMapping("/go-premium")
    public String showPlansPage(Model model, HttpServletRequest request){

        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        model.addAttribute("subscriptions", subscriptions);

        return "plans";
    }



//     //saving changes to development
//     @RequestMapping("/error")
//     @ResponseBody
//     String error(HttpServletRequest request) {
//         return "<h1>Error occurred</h1>";
//     }




}

