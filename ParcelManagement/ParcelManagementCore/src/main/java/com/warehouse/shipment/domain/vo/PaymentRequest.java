package com.warehouse.shipment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
    Long parcelId;
    double price;
}
