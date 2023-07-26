package com.boardify.boardify.controller;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameInfoDTO;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.GameCategory;
import com.boardify.boardify.entities.GameMechanics;
import com.boardify.boardify.service.BoardGameAtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private final BoardGameAtlasService boardGameAtlasService;

    @Autowired
    public GameController(BoardGameAtlasService boardGameAtlasService) {
        this.boardGameAtlasService = boardGameAtlasService;
    }

    @PostMapping("/games")
    public void saveGame(@RequestBody Game game) {
        boardGameAtlasService.saveGame(game);
    }

    @GetMapping("/games/search")
    public BoardGameResponse searchGames(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPlayers", required = false) Integer minPlayers,
            @RequestParam(value = "maxPlayers", required = false) Integer maxPlayers,
            @RequestParam(value = "releaseYear", required = false) Integer releaseYear,
            @RequestParam(value = "playtime", required = false) Integer playtime
    ) {


        return boardGameAtlasService.searchGames(name, minPlayers, maxPlayers, releaseYear, playtime);
    }

    @GetMapping
    public String handleDefault() {
        return "Welcome to the Boardify API!";
    }

    @GetMapping("/categories")
    public List<GameCategory> getCategories() {
        return boardGameAtlasService.retrieveAllCategories();
    }

    @GetMapping("/mechanics")
    public List<GameMechanics> getMechanics() {
        return boardGameAtlasService.retrieveAllMechanics();
    }


    @GetMapping("/all-games")
    public ModelAndView getAllGames() {
        List<GameSearchResult> allGames = boardGameAtlasService.retrieveAllGames().getGames();
        List<GameInfoDTO> gameInfoList = new ArrayList<>();

        for (GameSearchResult game : allGames) {
            GameInfoDTO gameInfo = new GameInfoDTO();
            gameInfo.setName(game.getName());
            gameInfo.setMinPlayers(game.getMinPlayers());
            gameInfo.setMaxPlayers(game.getMaxPlayers());
            gameInfo.setMinPlaytime(game.getMinPlaytime());
            gameInfo.setMaxPlaytime(game.getMaxPlaytime());
            gameInfo.setMinAge(game.getMinAge());
            gameInfoList.add(gameInfo);
        }

        ModelAndView modelAndView = new ModelAndView("gamesgrid"); // The name of the Thymeleaf template (without .html extension)
        modelAndView.addObject("games", gameInfoList); // Add the list of games to the model

        return modelAndView;
    }

}
