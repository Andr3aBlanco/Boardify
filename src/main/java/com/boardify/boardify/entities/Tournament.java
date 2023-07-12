package com.boardify.boardify.entities;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentID;
    private String tournamentName;
    private Long organizerID;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private int status;
    private String signupStart;
    private String signupEnd;
    private String eventStart;
    private String eventEnd;
    private int currEnrolled;
    private int maxEnrolled;
    private String lastEdited;
    private String compLevel;
    private double  prize;
    private double entryFees;
}
