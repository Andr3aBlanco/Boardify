package com.boardify.boardify.entities;


import com.boardify.boardify.DTO.ChargeResponse;
import com.stripe.model.Charge;
import org.springframework.stereotype.Component;

@Component
public class ChargeMapper {

    public ChargeResponse toChargeResponse(Charge charge) {
        // Perform the necessary mapping from Charge to ChargeResponse
        ChargeResponse chargeResponse = new ChargeResponse();
        chargeResponse.setStatus(charge.getStatus());
        chargeResponse.setId(charge.getId());
        chargeResponse.setTransactionId(charge.getBalanceTransaction());
        // Map other properties as needed
        return chargeResponse;
    }
}
