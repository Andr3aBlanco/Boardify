package com.boardify.boardify.controller;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.service.BoardGameAtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
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

}
