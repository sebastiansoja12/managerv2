package com.warehouse.paypal.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
public class PaymentRequest {
    Long parcelId;
    double price;
}
