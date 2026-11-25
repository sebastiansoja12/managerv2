package com.warehouse.billing.payment.infrastructure.adapter.primary.api;

import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.time.Instant;

public record ExternalPaymentApiResponse(PaymentStatus status,
                                         BigDecimal amount,
                                         Currency currency,
                                         String paymentUrl,
                                         Instant expiresAt) {
}
