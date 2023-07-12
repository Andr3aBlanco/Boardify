package com.boardify.boardify.utilities;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.repository.GameRepository;
import com.boardify.boardify.service.BoardGameAtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameDataSyncTask implements ApplicationRunner {

    @Autowired
    private final BoardGameAtlasService boardGameAtlasService;

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    public GameDataSyncTask(BoardGameAtlasService boardGameAtlasService, GameRepository gameRepository) {
        this.boardGameAtlasService = boardGameAtlasService;
        this.gameRepository = gameRepository;
    }

    private boolean isInitialSync = true;

//    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000) // Run once immediately, then every 24 hours
//    public void syncGameData() {
//        if (isInitialSync || gameRepository.count() == 0) {
//            BoardGameResponse gameData = boardGameAtlasService.retrieveAllGames();
//
//            for (GameSearchResult result : gameData.getGames()) {
//                Game game = Game.builder()
//                        .apiId(result.getId())
//                        .name(result.getName())
//                        .build();
//                gameRepository.save(game);
//
//                System.out.println(result.getId());
//            }
//
//            isInitialSync = false;
//        }
//    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (gameRepository.count() == 0) {
            BoardGameResponse gameData = boardGameAtlasService.retrieveAllGames();

            System.out.println("Len of repsonse " + gameData.getGames().size());
            for (GameSearchResult result : gameData.getGames()) {
                Game game = Game.builder()
                        .apiId(result.getId())
                        .name(result.getName())
                        .build();
                gameRepository.save(game);

                System.out.println(result.getId());
            }
        } else {
            System.out.println("game repository is not zero");
        }
    }
}
