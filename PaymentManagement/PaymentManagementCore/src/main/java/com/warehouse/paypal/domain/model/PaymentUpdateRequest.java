package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentUpdateRequest {
    String payerId;
    String paymentId;
}
