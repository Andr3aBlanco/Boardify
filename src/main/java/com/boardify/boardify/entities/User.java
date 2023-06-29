package com.boardify.boardify.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String username;
    private int role;
    private String firstName;
    private String lastaName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
    private String stripeToken;
    private int accountStatus;
    private int subscriptionType;
    private String subscriptionEnd;



}
