package com.boardify.boardify.DTO;

import com.stripe.param.checkout.SessionCreateParams;
import lombok.Data;

import java.util.Currency;

@Data
public class ChargeRequest {

    private String cardNumber;
    private String cardHolderName;
    private String expirationMonth;
    private String expirationYear;
    private String cvc;
    private double amount;
    private String currency;
}
