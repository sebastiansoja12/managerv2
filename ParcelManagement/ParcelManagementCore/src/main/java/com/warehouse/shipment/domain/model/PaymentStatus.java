package com.warehouse.shipment.domain.model;

import lombok.Data;

@Data
public class PaymentStatus {
    String link;
    String createTime;
    String failureReason;
    String paymentMethod;
}
