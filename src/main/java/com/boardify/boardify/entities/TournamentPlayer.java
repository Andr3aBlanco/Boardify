package com.boardify.boardify.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tournament_players")
public class TournamentPlayer {

    @EmbeddedId
    TournamentPlayerKey id;

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name = "tournament_id")
    Tournament tournament;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "player_id")
    User player; // refers to user table

    int organizerRating;

    int tournamentRating;

    int placement;
}
