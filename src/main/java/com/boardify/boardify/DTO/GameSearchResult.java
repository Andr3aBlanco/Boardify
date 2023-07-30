package com.boardify.boardify.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameSearchResult {

    private String id;
    private String name;
    private int min_players;
    private int max_players;
    private int min_playtime;
    private int max_playtime;
    private int min_age;
    private String image_url;
    private String description;

}

