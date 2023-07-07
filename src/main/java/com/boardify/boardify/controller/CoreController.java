package com.boardify.boardify.controller;



import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CoreController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {
        // Add any necessary logic or data retrieval here
        // For example, you can fetch some data to display on the home page

        // Add the necessary data to the model
        model.addAttribute("message", "Welcome to the Home Page!");

        // Manually add request as a context variable
        model.addAttribute("request", request);

        // Return the name of the Thymeleaf template for the home page
        return "home";
    }

    @GetMapping("/join-tournament")
    public String showJoinTournamentPage(Model model, HttpServletRequest request) {
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);

        return "join-tournament";
    }

    @GetMapping("/create-tournament")
    public String showCreateTournamentPage(Model model, HttpServletRequest request) {
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);

        return "create-tournament";
    }

    @GetMapping("/leaderboard")
    public String showLeaderboardPage(Model model, HttpServletRequest request) {
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);

        return "leaderboard";
    }

    @GetMapping("/error")
    public String handleError() {
        // Handle the error and provide a custom error page or redirect
        return "home"; // Replace "error" with the appropriate template name or redirect path
    }


    @GetMapping("/go-premium")
    public String showPlansPage(Model model, HttpServletRequest request){

        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        model.addAttribute("subscriptions", subscriptions);

        return "plans";
    }

    //saving changes to development
}

