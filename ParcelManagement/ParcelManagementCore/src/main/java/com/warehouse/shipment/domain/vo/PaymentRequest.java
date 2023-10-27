package com.warehouse.shipment.domain.vo;

import lombok.Value;

@Value
public class PaymentRequest {
    Long parcelId;
    double price;
}
