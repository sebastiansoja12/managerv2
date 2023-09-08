package com.warehouse.paypal.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentRequest {
    Long parcelId;
    BigDecimal price;
}
