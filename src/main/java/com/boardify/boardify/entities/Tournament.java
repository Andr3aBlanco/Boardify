package com.boardify.boardify.entities;


import com.boardify.boardify.service.UserService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
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

    @ManyToMany
    @JoinTable(
            name = "tournament_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;
    @ManyToMany
    @JoinTable(
            name = "tournament_games",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> games;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;
    @Column(name="state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private int status;

    /*@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name= "event_start")
    private String eventStart;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="event_end")
    private String eventEnd;*/

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "event_start")
    private LocalDate eventStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "event_end")
    private LocalDate eventEnd;


    private int currEnrolled;
    private int maxEnrolled;

    private String lastEdited;
    @Column(name="comp_level")
    private String compLevel;
    private double  prize;
    private double entryFees;
    @Column(name = "organizer_id")
    private Long organizerId;

    // Add the gameId getter and setter methods
    public Long getGameId() {
        return game != null ? game.getGameId() : null;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
    // Inside the Tournament class
    @Column(name = "user_rating")
    private int userRating;

    // Add getter and setter for userRating
    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
    @Transient
    private List<Tournament> tournaments;

    // Getter and Setter for the tournaments property
    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }


}
