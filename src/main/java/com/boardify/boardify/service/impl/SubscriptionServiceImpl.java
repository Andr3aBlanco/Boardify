package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.repository.SubscriptionRepository;
import com.boardify.boardify.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);

    }

    @Override
    public Optional<Subscription> findSubscriptionByID(Long id) {
        return subscriptionRepository.findById(id);
    }
}

