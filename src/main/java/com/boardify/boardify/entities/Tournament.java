package com.boardify.boardify.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
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

    // Other fields

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    private Game game;

    @ManyToMany
    @JoinTable(
            name = "tournament_players",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;
@Column(name="state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private int status;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name= "event_start")
    private String eventStart;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="event_end")
    private String eventEnd;

    private int currEnrolled;
    private int maxEnrolled;

    private String lastEdited;
    @Column(name="comp_level")
    private String compLevel;
    private double  prize;
    private double entryFees;
    @Column(name = "organizer_id")
    private Long organizerId;




}
