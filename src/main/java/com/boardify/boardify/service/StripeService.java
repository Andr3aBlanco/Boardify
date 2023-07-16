package com.boardify.boardify.service;


import com.boardify.boardify.DTO.ChargeRequest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    public Charge charge(ChargeRequest chargeRequest) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100)); // Stripe expects the amount in cents
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("source", chargeRequest.getCardNumber());
        chargeParams.put("description", "Charge for " + chargeRequest.getCardHolderName());

        return Charge.create(chargeParams);
    }
}
