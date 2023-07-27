package com.boardify.boardify.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private Long tournamentId;

    private String tournamentName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private Game game;

    @Transient
    private Long gameId; // Add the gameId property
    // Properties for storing tournament and host ratings



    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "event_start")
    private Date eventStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "event_end")
    private String eventEnd;

    private int currEnrolled;
    private int maxEnrolled;

    private String lastEdited;

    @Column(name = "comp_level")
    private String compLevel;
    private double prize;
    private double entryFees;

    @Column(name = "organizer_id")
        private Long organizerId;

   @Column(name = "tournament_rating")
    private Double tournamentRating;

    @Column(name = "host_rating")
    private Double hostRating;
    //Tournament_players
    @ManyToMany
    @JoinTable(
            name = "tournament_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;
    //Tournament_games
    @ManyToMany
    @JoinTable(
            name = "tournament_games",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> games;
    @ManyToMany
    @JoinTable(
            name= "ratings",
            joinColumns = @JoinColumn(name = "tournament_rating"),
            inverseJoinColumns = @JoinColumn(name = "host_Rating")

    )
    private List<Tournament> tournamentRatings; // not sure if this should be like that

    // Add the gameId getter and setter methods
    public Long getGameId() {
        return game != null ? game.getGameId() : null;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
