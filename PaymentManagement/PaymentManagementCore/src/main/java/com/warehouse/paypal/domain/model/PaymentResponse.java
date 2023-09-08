package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;


@Data
@Builder
public class PaymentResponse {
    private Link link;
    private String createTime;
    private String failureReason;
    private String paymentMethod;
}
