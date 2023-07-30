package com.boardify.boardify.controller;




import com.boardify.boardify.entities.*;
import com.boardify.boardify.DTO.UserDto;

import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.GameService;


import com.boardify.boardify.service.SubscriptionService;
import com.boardify.boardify.service.TournamentService;
import com.boardify.boardify.service.TransactionService;
import com.boardify.boardify.service.UserService;
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


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.text.SimpleDateFormat;
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


    //private final UserRepository userRepository;
    @Autowired

    private GameService gameService;

    public CoreController(UserRepository userRepository) {
        // this.userRepository = userRepository;
    }
    

    @Autowired
    private TransactionService transactionService;


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
        Date today = new Date();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();// RETURNS THE EMAIL(PRIMARY KEY)
            User user = userService.findByEmail(email);

            if (user != null) {
                String username = user.getUsername();

                // Add the necessary data to the model
                model.addAttribute("username", username);

                if (user.getId() != null)
                {
                    Long myId = user.getId();
                    model.addAttribute("userId",myId);
                    List<Tournament> myTournaments = tournamentService.findAllOpenTournamentsByUser(today,user.getId());

                    model.addAttribute("myTournaments",myTournaments);
                    List<Tournament> pastTournaments = tournamentService.findAllTournamentsBeforeTodayAndUser(today, user.getId());
                    model.addAttribute("pastTournaments", pastTournaments);
                }
            }
        }

        /*List<Tournament> tournaments = tournamentService.findAll();
        model.addAttribute("tournaments", tournaments);*/
        List<Tournament> openTournaments = tournamentService.findAllOpenTournaments(today);
        model.addAttribute("openTournaments",openTournaments);

        /*List<Tournament> myCreatedOpenTournaments = tournamentService.findAllOpenTournamentsByUser(today,email);
        model.addAttribute("myTournaments",myCreatedOpenTournaments);
*/
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
                } else if (role.equals("ROLE_PREMIUM")) {
                    model.addAttribute("request", request);
                    Tournament tournament = new Tournament();
                    model.addAttribute("tournament", tournament);
                    List<Game> games = gameService.findAll();
                    model.addAttribute("games", games);
                    return "create-tournament";
                } else if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/";
                }
            } else {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
        return "redirect:/login";
//                    model.addAttribute("request", request);
//                    Tournament tournament = new Tournament();
//                    model.addAttribute("tournament", tournament);
//                    List<Game> games = gameService.findAll();
//                    model.addAttribute("games", games);
//                    System.out.println("premium");
//                    return "create-tournament";
    }


    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        List<Tournament> allTournaments = tournamentService.findAllTournaments();
        model.addAttribute("tournaments", allTournaments);

        List<UserDto> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);

        return "leaderboard";
    }
/* If you don't need custom error handling I'm going to comment this
    @GetMapping("/error")
    public String handleError() {
        // Get the error status code

        // Your custom error handling code here
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

//    @GetMapping("/transactions")
//    public String showTransactionsPage(Model model) {
//        List<Transaction> transactions = transactionService.findAllTransactions();
//        model.addAttribute("transactions", transactions);
//        return "transactions";
//    }


 */
    @GetMapping("/transactions")
    public String showTransactionsPage(@RequestParam Map<String, String> customQuery, Model model) {
        List<Transaction> transactions;
        System.out.println(customQuery.get("item"));
        if (customQuery.size() < 1) {
            transactions = transactionService.findAllTransactions();
        } else {
            transactions = transactionService.findByFilter(customQuery);
        }
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

