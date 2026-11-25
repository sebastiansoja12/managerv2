package com.warehouse.billing.payment.application.port.primary.result;

import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.model.Money;

import java.time.Instant;

public record CheckoutView(PaymentStatus status, Money amount, String paymentUrl, Instant expiresAt) {
}
