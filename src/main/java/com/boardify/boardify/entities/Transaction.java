package com.boardify.boardify.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    @Temporal(TemporalType.DATE)
    Date transactionDate;

    @Temporal(TemporalType.TIME)
    Date transactionTime;

    double amount;
    // transactionType represents sale or refund
    String transactionType;

    String item;

    double commission;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "tournament_id", nullable = true)
    private Tournament tournament;

}
