package com.boardify.boardify.DTO;

import lombok.Data;

@Data
public class ChargeResponse {
    private boolean success;
    private String message;
    private String paymentId;
    private String errorMessage;
}
