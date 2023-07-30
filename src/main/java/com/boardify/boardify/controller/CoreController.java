package com.boardify.boardify.controller;




import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.entities.User;

import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.*;


import com.boardify.boardify.entities.Tournament;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.List;
import java.util.Map;

@Controller
public class CoreController implements ErrorController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired

    private TournamentService tournamentService;


    //private final UserRepository userRepository;
    @Autowired

    private GameService gameService;

    public CoreController(UserRepository userRepository) {
       // this.userRepository = userRepository;
    }

    private TransactionService transactionService;

    @Autowired
    private BoardGameAtlasService atlasService;



    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);
            if (user != null) {
                String username = user.getUsername();
                // Add the necessary data to the model
                model.addAttribute("username", username);
                model.addAttribute("message", "Hello " + username + "!");
            }



        }

        BoardGameResponse allGames = atlasService.retrieveFour();
        List<GameSearchResult> games = allGames.getGames();
        model.addAttribute("gamesList", games);


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
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);
            if (user != null) {
                String username = user.getUsername();
                // Add the necessary data to the model
                model.addAttribute("username", username);

            }
        }
        Date today = new Date();
        List<Tournament> pastTournaments = tournamentService.findAllTournamentsBeforeToday(today);
        model.addAttribute("pastTournaments", pastTournaments);
        /*List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);*/
        List<Tournament> openTournaments = tournamentService.findAllOpenTournaments(today);
        model.addAttribute("openTournaments",openTournaments);
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

    @GetMapping("/error")
    public String handleError() {
        // Handle the error and provide a custom error page or redirect
        return "redirect:/"; // Replace "error" with the appropriate template name or redirect path
    }


//    @GetMapping("/go-premium")
//    public String showPlansPage(Model model, HttpServletRequest request){
//
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        Object obj = auth.getPrincipal();
////        model.addAttribute("testUser", obj);
////        UserDetails userDetails = (UserDetails) auth.getPrincipal();
////        String userEmail = userDetails.getUsername();
////        UserDto userDto = userService.convertEntityToDto(userService.findByEmail(userEmail));
////        model.addAttribute("currentUser", userDto);
//        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
//        model.addAttribute("subscriptions", subscriptions);
//
//        return "plans";
//    }

    @GetMapping("/edit-plan")
    public String showEditPlanPage(Model model, HttpServletRequest request){

        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        model.addAttribute("subscriptions", subscriptions);

        return "edit-plan";
    }

    @GetMapping("/transactions")
    public String showTransactionsPage(Model model) {
        List<Transaction> transactions = transactionService.findAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/transactions/filter")
    public String showTransactionsPageFiltered(@RequestParam Map<String, String> customQuery, Model model) {
        List<Transaction> transactions = transactionService.findByFilter(customQuery);
//        System.out.println(transactions.get(0));
        System.out.println(customQuery.get("item"));
        model.addAttribute("transactions", transactions);
        return "transactions";
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

