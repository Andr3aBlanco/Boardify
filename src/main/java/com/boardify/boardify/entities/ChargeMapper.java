package com.boardify.boardify.entities;


import com.boardify.boardify.DTO.ChargeResponse;
import com.stripe.model.Charge;
import org.springframework.stereotype.Component;

@Component
public class ChargeMapper {

    public ChargeResponse toChargeResponse(Charge charge) {
        ChargeResponse chargeResponse = new ChargeResponse();
        chargeResponse.setSuccess(charge.getPaid());
        chargeResponse.setMessage(charge.getPaid() ? "Payment successful" : "Payment failed");
        chargeResponse.setPaymentId(charge.getId());
        chargeResponse.setErrorMessage(charge.getPaid() ? null : charge.getFailureMessage());
        return chargeResponse;
    }
}
