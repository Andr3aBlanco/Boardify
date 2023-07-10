package com.boardify.boardify.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    //For authentication
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // AUTH ends here

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
