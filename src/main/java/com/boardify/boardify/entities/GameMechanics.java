package com.boardify.boardify.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "game_mechanics")
public class GameMechanics {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private String categoryId;
    private String name;

}
