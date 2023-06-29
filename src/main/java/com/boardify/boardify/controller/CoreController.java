package com.boardify.boardify.controller;



import com.boardify.boardify.entities.Tournament;
import com.boardify.boardify.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CoreController {

    @Autowired
    private TournamentService tournamentService;


    @GetMapping("/")
    public String showHomePage(Model model) {
        // Add any necessary logic or data retrieval here
        // For example, you can fetch some data to display on the home page

        // Add the necessary data to the model
        model.addAttribute("message", "Welcome to the Home Page!");

        // Return the name of the Thymeleaf template for the home page
        return "home";
    }


    @GetMapping("/tournaments")
 public String getAllTournaments(Model model){

     List<Tournament> tournaments = tournamentService.findAll();
     model.addAttribute("tournaments", tournaments);
     return "home";

 }
}
