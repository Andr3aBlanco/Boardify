package com.boardify.boardify.service;

import com.boardify.boardify.entities.Game;

import java.util.List;

public interface GameService {
    List<Game> findAll();
    Game findGameById(Long gameId); // Add this method
}
