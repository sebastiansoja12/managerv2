package com.warehouse.billing.payment.domain.model;

import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.model.Money;

import java.time.Instant;

public record LateChargeRefund(String gatewayReference, String gatewayPaymentIntent, PaymentId paymentId,
                               Money amount, Instant refundedAt) {
}
