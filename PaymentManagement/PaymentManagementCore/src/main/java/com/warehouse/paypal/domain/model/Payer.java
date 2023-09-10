package com.warehouse.paypal.domain.model;

import com.warehouse.paypal.domain.enumeration.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payer {
    private PaymentMethod paymentMethod = PaymentMethod.PAYPAL;
    private String status;
    private String accountType;
    private String accountAge;
    private PayerInfo payerInfo;
}
