package com.boardify.boardify.utilities;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.repository.GameRepository;
import com.boardify.boardify.service.BoardGameAtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameDataSyncTask {

    private final BoardGameAtlasService boardGameAtlasService;
    private final GameRepository gameRepository;

    @Autowired
    public GameDataSyncTask(BoardGameAtlasService boardGameAtlasService, GameRepository gameRepository) {
        this.boardGameAtlasService = boardGameAtlasService;
        this.gameRepository = gameRepository;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run once every 24 hours
    public void syncGameData() {
        BoardGameResponse gameData = boardGameAtlasService.retrieveAllGames();

        for (GameSearchResult result : gameData.getGames()) {
            Game game = Game.builder()
                    .gameId(Long.valueOf(result.getId()))
                    .name(result.getName())
                    .build();
            gameRepository.save(game);
        }
    }
}
