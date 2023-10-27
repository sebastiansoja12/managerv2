package com.warehouse.shipment.domain.vo;

import lombok.Value;

@Value
public class PaymentResponse {
    String paymentUrl;
    String createTime;
    String failureReason;
    String paymentMethod;
}
