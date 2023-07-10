package com.boardify.boardify.service;

import com.boardify.boardify.entities.Subscription;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
public interface SubscriptionService {

    List<Subscription> findAllSubscriptions();
    Subscription createSubscription(Subscription subscription);

    void updateSubscription(Subscription subscription);

    void deleteSubscription(Long id);

    Optional<Subscription> findSubscriptionByID(Long id);
}
