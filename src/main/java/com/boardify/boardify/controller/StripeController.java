package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.ChargeRequest;
import com.boardify.boardify.DTO.ChargeResponse;
import com.boardify.boardify.entities.ChargeMapper;
import com.boardify.boardify.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StripeController {
    private final StripeService stripeService;
    private final ChargeMapper chargeMapper;

    @PostMapping(value = "/charge")
    public ChargeResponse charge(@RequestBody ChargeRequest chargeRequest)
            throws StripeException {
        Charge charge = stripeService.charge(chargeRequest);
        return chargeMapper.toChargeResponse(charge);
    }
}
