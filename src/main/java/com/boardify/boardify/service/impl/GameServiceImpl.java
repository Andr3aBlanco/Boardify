package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Game;
import com.boardify.boardify.repository.GameRepository;
import com.boardify.boardify.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game findGameById(Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        return game.orElse(null);
    }

    public List<Game> findAll() {
        Sort sortByGameName = Sort.by(Sort.Direction.ASC, "name"); // Sort by the "name" property of the "game" association
        return gameRepository.findAll(sortByGameName);
    }
}
