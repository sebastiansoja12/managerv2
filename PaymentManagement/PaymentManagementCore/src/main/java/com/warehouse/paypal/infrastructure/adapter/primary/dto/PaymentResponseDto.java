package com.warehouse.paypal.infrastructure.adapter.primary.dto;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private LinkDto link;
    private String createTime;
    private String failureReason;
    private String paymentMethod;
}
