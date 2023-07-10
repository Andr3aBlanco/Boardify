package com.boardify.boardify.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table( name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;

    private double cost;
    private String duration;
    private String subscriptionName;
    private double commissionRate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Double.compare(that.cost, cost) == 0 && Double.compare(that.commissionRate, commissionRate) == 0 && Objects.equals(subscriptionID, that.subscriptionID) && Objects.equals(duration, that.duration) && Objects.equals(subscriptionName, that.subscriptionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionID, cost, duration, subscriptionName, commissionRate);
    }
}

