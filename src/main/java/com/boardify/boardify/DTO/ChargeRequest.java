package com.boardify.boardify.DTO;

import com.stripe.param.checkout.SessionCreateParams;
import lombok.Data;

import java.util.Currency;

@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD, CAD;
    }
    private String description;
    private int amount;
    private Currency currency;
//    private String stripeEmail;
    private String stripeToken;
}
