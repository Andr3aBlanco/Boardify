package com.boardify.boardify.utilities;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.GameCategory;
import com.boardify.boardify.entities.GameMechanics;
import com.boardify.boardify.repository.GameCategoryRepository;
import com.boardify.boardify.repository.GameMechanicsRepository;
import com.boardify.boardify.repository.GameRepository;
import com.boardify.boardify.service.BoardGameAtlasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameDataSyncTask implements ApplicationRunner {

    @Autowired
    private final BoardGameAtlasService boardGameAtlasService;

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final GameCategoryRepository gameCategoryRepository;

    @Autowired
    private final GameMechanicsRepository gameMechanicsRepository;

    private boolean isGamesInitialSync = true;
    private boolean isCategoriesInitialSync = true;
    private boolean isMechanicsInitialSync = true;


    public GameDataSyncTask(BoardGameAtlasService boardGameAtlasService, GameRepository gameRepository, GameCategoryRepository gameCategoryRepository, GameMechanicsRepository gameMechanicsRepository) {
        this.boardGameAtlasService = boardGameAtlasService;
        this.gameRepository = gameRepository;
        this.gameCategoryRepository = gameCategoryRepository;
        this.gameMechanicsRepository = gameMechanicsRepository;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (gameRepository.count() == 0) {
            performGamesInitialSync();
        } else {
            System.out.println("Game repository is not empty");
        }

        if (gameCategoryRepository.count() == 0) {
            performCategoriesInitialSync();
        } else {
            System.out.println("Game category repository is not empty");
        }

        if (gameMechanicsRepository.count() == 0) {
            performMechanicsInitialSync();
        } else {
            System.out.println("Game mechanics repository is not empty");
        }
    }

    @Scheduled(fixedDelay = 604800000) // Run every week (7 days = 7 * 24 * 60 * 60 * 1000 milliseconds)
    private void scheduledSync() {
        performGamesSync();
        performCategoriesSync();
        performMechanicsSync();
    }

    private void performGamesInitialSync() {
        BoardGameResponse gameData = boardGameAtlasService.retrieveAllGames();
        saveGames(gameData);
        isGamesInitialSync = false;
    }

    private void performCategoriesInitialSync() {
        List<GameCategory> categories = boardGameAtlasService.retrieveAllCategories();
        gameCategoryRepository.saveAll(categories);
        isCategoriesInitialSync = false;
    }

    private void performMechanicsInitialSync() {
        List<GameMechanics> mechanics = boardGameAtlasService.retrieveAllMechanics();
        gameMechanicsRepository.saveAll(mechanics);
        isMechanicsInitialSync = false;
    }

    private void performGamesSync() {
        if (!isGamesInitialSync) {
            List<String> remoteGameIds = boardGameAtlasService.retrieveAllGameIds();
            List<String> localGameIds = gameRepository.findAllGameIds();
            List<String> newGameIds = getNewIds(remoteGameIds, localGameIds);

            if (!newGameIds.isEmpty()) {
                List<GameSearchResult> newGames = boardGameAtlasService.retrieveGamesByIds(newGameIds);
                saveGames(new BoardGameResponse(newGames));
            }
        }
    }

    private void performCategoriesSync() {
        if (!isCategoriesInitialSync) {
            List<GameCategory> remoteCategories = boardGameAtlasService.retrieveAllCategories();
            gameCategoryRepository.saveAll(remoteCategories);
        }
    }

    private void performMechanicsSync() {
        if (!isMechanicsInitialSync) {
            List<GameMechanics> remoteMechanics = boardGameAtlasService.retrieveAllMechanics();
            gameMechanicsRepository.saveAll(remoteMechanics);
        }
    }

    private List<String> getNewIds(List<String> remoteIds, List<String> localIds) {
        Set<String> remoteSet = new HashSet<>(remoteIds);
        Set<String> localSet = new HashSet<>(localIds);

        // Calculate new IDs by finding the difference between remote and local sets
        remoteSet.removeAll(localSet);
        return new ArrayList<>(remoteSet);
    }

    private void saveGames(BoardGameResponse gameData) {
        List<GameSearchResult> games = gameData.getGames();
        List<Game> gameEntities = games.stream()
                .map(result -> Game.builder().apiId(result.getId()).name(result.getName()).build())
                .collect(Collectors.toList());
        gameRepository.saveAll(gameEntities);
    }


}
