package com.boardify.boardify.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TournamentPlayerKey implements Serializable {

    @Column(name="tournament_id")
    Long tournamentId;

    @Column(name = "player_id")
    Long playerId;

}
