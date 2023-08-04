package com.boardify.boardify.service;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.DTO.CategoryResponse;
import com.boardify.boardify.DTO.GameSearchResult;
import com.boardify.boardify.DTO.MechanicsResponse;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.entities.GameCategory;
import com.boardify.boardify.entities.GameMechanics;
import com.boardify.boardify.repository.GameCategoryRepository;
import com.boardify.boardify.repository.GameMechanicsRepository;
import com.boardify.boardify.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BoardGameAtlasService {

    @Value("${boardgameatlas.api.clientid}")
    private String apiKey;

    private static final String BASE_URL = "https://api.boardgameatlas.com/api/search";

    private final RestTemplate restTemplate;
    private final GameRepository gameRepository;
    private final GameCategoryRepository gameCategoryRepository;
    private final GameMechanicsRepository gameMechanicsRepository;


    @Autowired
    public BoardGameAtlasService(RestTemplate restTemplate, GameRepository gameRepository,
                                 GameCategoryRepository gameCategoryRepository,
                                 GameMechanicsRepository gameMechanicsRepository) {
        this.restTemplate = restTemplate;
        this.gameRepository = gameRepository;
        this.gameCategoryRepository = gameCategoryRepository;
        this.gameMechanicsRepository = gameMechanicsRepository;
    }

    public BoardGameResponse searchGames(String gameName, Integer minPlayers, Integer maxPlayers, Integer releaseYear, Integer playtime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParamIfPresent("name", Optional.ofNullable(gameName));

        if (minPlayers != null) {
            builder.queryParam("min_players", minPlayers);
        }
        if (maxPlayers != null) {
            builder.queryParam("max_players", maxPlayers);
        }
        if (releaseYear != null) {
            builder.queryParam("year_published", releaseYear);
        }
        if (playtime != null) {
            builder.queryParam("min_playtime", playtime);
        }

        builder.queryParam("client_id", apiKey);

        String url = builder.toUriString();

        System.out.println("Generated URI: " + url);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, BoardGameResponse.class);
    }



    public BoardGameResponse searchGamesByName(String name) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("name", name)
                .queryParam("fuzzy_match", true)
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, BoardGameResponse.class);
    }

    public BoardGameResponse searchGamesByPlayerCount(int minPlayers, int maxPlayers) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("min_players", minPlayers)
                .queryParam("max_players", maxPlayers)
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, BoardGameResponse.class);
    }

    public BoardGameResponse searchGamesByPlayTime(int minPlayTime, int maxPlayTime) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("min_playtime", minPlayTime)
                .queryParam("max_playtime", maxPlayTime)
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, BoardGameResponse.class);
    }

    public BoardGameResponse retrieveAllGames() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("client_id", apiKey)
                .queryParam("limit", 100)
                .build()
                .toUriString();

        System.out.println("Calling retrieve all games " + url);

        return restTemplate.getForObject(url, BoardGameResponse.class);
    }


    public BoardGameResponse retrieveFour() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("client_id", apiKey)
                .queryParam("limit", 4)
                .queryParam("order_by", "rank")
                .build()
                .toUriString();

        System.out.println("Calling retrieve all games " + url);

        return restTemplate.getForObject(url, BoardGameResponse.class);
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }


    public CategoryResponse retrieveAllCategories() {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.boardgameatlas.com/api/game/categories")
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

        System.out.println("This is the url for categories" + url);

        ResponseEntity<CategoryResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, CategoryResponse.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            return restTemplate.getForObject(url, CategoryResponse.class);
        } else {
            throw new RuntimeException("Failed to retrieve categories data from BoardGameAtlas API.");
        }
    }


    public MechanicsResponse retrieveAllMechanics() {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.boardgameatlas.com/api/game/mechanics")
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

        ResponseEntity<MechanicsResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, MechanicsResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return restTemplate.getForObject(url, MechanicsResponse.class);
        } else {
            throw new RuntimeException("Failed to retrieve mechanics data from BoardGameAtlas API.");
        }
    }



    public List<String> retrieveAllGameIds() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("client_id", apiKey)
                .queryParam("limit", 100)
                .build()
                .toUriString();

        BoardGameResponse response = restTemplate.getForObject(url, BoardGameResponse.class);
        if (response != null && response.getGames() != null) {
            return response.getGames().stream().map(GameSearchResult::getId).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Failed to retrieve game data from BoardGameAtlas API.");
        }
    }

    public List<GameSearchResult> retrieveGamesByIds(List<String> gameIds) {
        List<GameSearchResult> games = new ArrayList<>();
        int batchSize = 100;
        int fromIndex = 0;
        int toIndex = Math.min(batchSize, gameIds.size());

        while (fromIndex < gameIds.size()) {
            List<String> batchIds = gameIds.subList(fromIndex, toIndex);
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("ids", String.join(",", batchIds))
                    .queryParam("client_id", apiKey)
                    .build()
                    .toUriString();

            BoardGameResponse response = restTemplate.getForObject(url, BoardGameResponse.class);
            if (response != null && response.getGames() != null) {
                games.addAll(response.getGames());
            }

            fromIndex = toIndex;
            toIndex = Math.min(toIndex + batchSize, gameIds.size());
        }

        return games;
    }

}
