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


@Service
public class BoardGameAtlasService {

    @Value("${boardgameatlas.api.clientid}")
    private String apiKey;

    private static final String BASE_URL = "https://api.boardgameatlas.com/api/search";

    private final RestTemplate restTemplate;
    private final GameRepository gameRepository;

    public BoardGameResponse searchGames(String gameName, int minPlayers, int maxPlayers, int releaseYear, int playtime) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.boardgameatlas.com/api/search")
                .queryParam("name", gameName)
                .queryParam("min_players", minPlayers)
                .queryParam("max_players", maxPlayers)
                .queryParam("year_published", releaseYear)
                .queryParam("min_playtime", playtime)
                .queryParam("client_id", apiKey)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, BoardGameResponse.class);
    }


    public BoardGameAtlasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        gameRepository = null;
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
                .queryParam("limit", 1000) // Adjust the limit based on your needs
                .queryParam("client_id", apiKey)
                .build()
                .toUriString();

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
