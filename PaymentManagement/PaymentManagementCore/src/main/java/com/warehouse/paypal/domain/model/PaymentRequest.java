package com.warehouse.paypal.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentRequest {
    private Long parcelId;
    private BigDecimal price;
    private Payer payer;
}
