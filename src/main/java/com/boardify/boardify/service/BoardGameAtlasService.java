package com.boardify.boardify.service;


import com.boardify.boardify.DTO.BoardGameResponse;
import com.boardify.boardify.entities.Game;
import com.boardify.boardify.repository.GameRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@Service
public class BoardGameAtlasService {

    @Value("${boardgameatlas.api.clientid}")
    private String apiKey;

    private static final String BASE_URL = "https://api.boardgameatlas.com/api/search";

    private final RestTemplate restTemplate;
    private final GameRepository gameRepository;

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




    public BoardGameAtlasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.gameRepository = null;
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


    @Autowired
    public BoardGameAtlasService(RestTemplate restTemplate, GameRepository gameRepository) {
        this.restTemplate = restTemplate;
        this.gameRepository = gameRepository;
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }
}
