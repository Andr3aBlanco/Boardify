package com.boardify.boardify.DTO;

import lombok.Data;

import java.util.List;

@Data
public class BoardGameResponse {

    private List<GameSearchResult> games;

}

