package com.boardify.boardify.controller;



import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import com.boardify.boardify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CoreController implements ErrorController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TournamentService tournamentService;

    //private final UserRepository userRepository;

    @Autowired
    private UserService userService;


//    public CoreController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
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


    @GetMapping("/join-tournament")
    public String showJoinTournamentPage(Model model, HttpServletRequest request) {

        List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);

        return "join-tournament";
    }

    @GetMapping("/create-tournament")
    public String showCreateTournamentPage(Model model, HttpServletRequest request) {
        // Add necessary logic or data retrieval here

        // Manually add request as a context variable
        model.addAttribute("request", request);
        Tournament tournament = new Tournament();
        model.addAttribute("tournament", tournament);
        return "create-tournament";
    }

//    @GetMapping("/leaderboard")
//    public String showLeaderboardPage(Model model, HttpServletRequest request) {
//        // Add necessary logic or data retrieval here
//
//        // Manually add request as a context variable
//        model.addAttribute("request", request);
//
//        return "leaderboard";
//    }


    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        List<Tournament> allTournaments = tournamentService.findAllTournaments();
        model.addAttribute("tournaments", allTournaments);

        List<UserDto> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);

        return "leaderboard";
    }




//    @GetMapping("/error")
//    public String handleError() {
//        // Handle the error and provide a custom error page or redirect
//        return "redirect:/home"; // Replace "error" with the appropriate template name or redirect path
//    }

    @GetMapping("/error")
    public String handleError() {
        // Handle the error and provide a custom error page or redirect
        return "redirect:/"; // Replace "error" with the appropriate template name or redirect path
    }



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

