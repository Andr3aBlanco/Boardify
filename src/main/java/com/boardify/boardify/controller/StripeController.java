package com.boardify.boardify.controller;

import com.boardify.boardify.DTO.ChargeRequest;
import com.boardify.boardify.DTO.ChargeResponse;
import com.boardify.boardify.entities.ChargeMapper;
import com.boardify.boardify.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequiredArgsConstructor
public class StripeController {
    private final StripeService stripeService;
    private final ChargeMapper chargeMapper;

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostMapping(value = "/charge", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChargeResponse charge(@RequestBody ChargeRequest chargeRequest)
            throws StripeException {
        Charge charge = stripeService.charge(chargeRequest);
        return chargeMapper.toChargeResponse(charge);
    }

//    @RequestMapping("/paying")
//    public String returnPaying(){
//        return "<h1>Hello </h1>" + secretKey;
//    }
}
